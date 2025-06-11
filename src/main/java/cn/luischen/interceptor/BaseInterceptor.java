package cn.luischen.interceptor;

import cn.luischen.constant.Types;
import cn.luischen.constant.WebConst;
import cn.luischen.model.OptionsDomain;
import cn.luischen.model.UserDomain;
import cn.luischen.service.option.OptionService;
import cn.luischen.service.user.UserService;
import cn.luischen.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 自定义拦截器
 */
@Component
public class BaseInterceptor implements HandlerInterceptor {
    private static final Logger LOGGE = LoggerFactory.getLogger(BaseInterceptor.class);
    private static final String USER_AGENT = "user-agent";

    @Autowired
    private UserService userService;

    @Autowired
    private OptionService optionService;


    @Autowired
    private Commons commons;

    @Autowired
    private AdminCommons adminCommons;

    private MapCache cache = MapCache.single();


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String uri = normalizeRequestUri(request);

        // 规范化路径，防止路径遍历
        uri = uri.replaceAll("/+", "/");

        LOGGE.info("UserAgent: {}", request.getHeader(USER_AGENT));
        LOGGE.info("用户访问地址: {}, 来路地址: {}", uri, IPKit.getIpAddrByRequest(request));

        // 获取当前登录用户
        UserDomain user = TaleUtils.getLoginUser(request);
        if (user == null) {
            Integer uid = TaleUtils.getCookieUid(request);
            if (uid != null) {
                user = userService.getUserInfoById(uid);
                request.getSession().setAttribute(WebConst.LOGIN_SESSION_KEY, user);
            }
        }

        // 权限控制
        if (isAdminPath(uri)
                && !uri.matches("^/admin/login(/.*)?") // 严格匹配/admin/login路径
                && user == null
                && !isStaticResource(uri)) {

            response.sendRedirect(request.getContextPath() + "/admin/login");
            return false;
        }

        // 检查权限：对于敏感操作路径（如删除），确保用户是管理员
        if (isSensitiveOperation(uri) && user == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized access");
            return false;
        }

        // 设置 CSRF token，仅对敏感操作进行校验
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            String csrfToken = UUID.UU64();
            cache.hset(Types.CSRF_TOKEN.getType(), csrfToken, uri, 30 * 60);
            request.setAttribute("_csrf_token", csrfToken);
        } else if ("POST".equalsIgnoreCase(request.getMethod()) && isSensitiveOperation(uri) && user == null) {
            // 检查 POST 请求的 CSRF token
            String csrfToken = request.getParameter("_csrf_token");
            String expectedUri = cache.hget(Types.CSRF_TOKEN.getType(), csrfToken);
            if (expectedUri == null || !expectedUri.equals(uri)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "CSRF token invalid or expired.");
                return false;
            }
            cache.hdel(Types.CSRF_TOKEN.getType(), csrfToken);
        }

        return true;
    }

    private boolean isAdminPath(String uri) {
        return uri.matches("^/admin(/.*)?$");
    }

    private String normalizeRequestUri(HttpServletRequest request) throws UnsupportedEncodingException {
        String rawUri = request.getRequestURI();
        String decodedUri = deepDecode(rawUri);

        // 修复：移除分号及后续路径参数
        int semicolonIndex = decodedUri.indexOf(';');
        if (semicolonIndex > -1) {
            decodedUri = decodedUri.substring(0, semicolonIndex);
        }

        // 规范化路径（处理"."和".."）
        String normalized = Paths.get(decodedUri).normalize().toString().replace("\\", "/");
        return normalized.toLowerCase(Locale.ROOT);
    }


    public String deepDecode(String uri) throws UnsupportedEncodingException {
        String prev;
        String decoded = uri;
        do {
            prev = decoded;
            decoded = URLDecoder.decode(prev, "UTF-8");
        } while (!prev.equals(decoded));
        return decoded;
    }

    /**
     * 检查是否为静态资源文件
     */
    private boolean isStaticResource(String uri) {
        return uri.startsWith("/admin/css") || uri.startsWith("/admin/images")
                || uri.startsWith("/admin/js") || uri.startsWith("/admin/plugins")
                || uri.startsWith("/admin/editormd");
    }

    /**
     * 检查是否为敏感操作路径（例如：删除、更新等操作）
     */
    private boolean isSensitiveOperation(String uri) {
        return uri.contains("/delete") || uri.contains("/update") || uri.contains("/create");
    }




    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        OptionsDomain ov = optionService.getOptionByName("site_record");
        httpServletRequest.setAttribute("commons", commons);//一些工具类和公共方法
        httpServletRequest.setAttribute("option", ov);
        httpServletRequest.setAttribute("adminCommons", adminCommons);
        initSiteConfig(httpServletRequest);

    }

    private void initSiteConfig(HttpServletRequest request) {
        if (WebConst.initConfig.isEmpty()){
            List<OptionsDomain> options = optionService.getOptions();
            Map<String, String> querys = new HashMap<>();
            options.forEach(option -> {
                querys.put(option.getName(), option.getValue());
            });
            WebConst.initConfig = querys;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}

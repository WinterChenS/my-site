package cn.luischen.controller.admin;

import cn.luischen.constant.LogActions;
import cn.luischen.constant.WebConst;
import cn.luischen.controller.BaseController;
import cn.luischen.dto.StatisticsDto;
import cn.luischen.exception.BusinessException;
import cn.luischen.model.CommentDomain;
import cn.luischen.model.ContentDomain;
import cn.luischen.model.LogDomain;
import cn.luischen.model.UserDomain;
import cn.luischen.service.log.LogService;
import cn.luischen.service.site.SiteService;
import cn.luischen.service.user.UserService;
import cn.luischen.utils.APIResponse;
import cn.luischen.utils.GsonUtils;
import cn.luischen.utils.TaleUtils;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Donghua.Chen on 2018/4/30.
 */
@Api("后台首页")
@Controller("adminIndexController")
@RequestMapping(value = "/admin")
public class IndexController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private SiteService siteService;

    @Autowired
    private LogService logService;

    @Autowired
    private UserService userService;



    @ApiOperation("进入首页")
    @GetMapping(value = {"","/index"})
    public String index(HttpServletRequest request){
        LOGGER.info("Enter admin index method");
        List<CommentDomain> comments = siteService.getComments(5);
        List<ContentDomain> contents = siteService.getNewArticles(5);
        StatisticsDto statistics = siteService.getStatistics();
        // 取最新的20条日志
        PageInfo<LogDomain> logs = logService.getLogs(1, 5);
        List<LogDomain> list = logs.getList();
        request.setAttribute("comments", comments);
        request.setAttribute("articles", contents);
        request.setAttribute("statistics", statistics);
        request.setAttribute("logs", list);
        LOGGER.info("Exit admin index method");
        return "admin/index";
    }

    /**
     * 个人设置页面
     */
    @GetMapping(value = "/profile")
    public String profile() {
        return "admin/profile";
    }


    /**
     * 保存个人信息
     */
    @PostMapping(value = "/profile")
    @ResponseBody
    public APIResponse saveProfile(@RequestParam String screenName, @RequestParam String email, HttpServletRequest request, HttpSession session) {
        UserDomain users = this.user(request);
        if (StringUtils.isNotBlank(screenName) && StringUtils.isNotBlank(email)) {
            UserDomain temp = new UserDomain();
            temp.setUid(users.getUid());
            temp.setScreenName(screenName);
            temp.setEmail(email);
            userService.updateUserInfo(temp);
            logService.addLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));

            //更新session中的数据
            UserDomain original= (UserDomain) session.getAttribute(WebConst.LOGIN_SESSION_KEY);
            original.setScreenName(screenName);
            original.setEmail(email);
            session.setAttribute(WebConst.LOGIN_SESSION_KEY,original);
        }
        return APIResponse.success();
    }

    /**
     * 修改密码
     */
    @PostMapping(value = "/password")
    @ResponseBody
    public APIResponse upPwd(@RequestParam String oldPassword, @RequestParam String password, HttpServletRequest request,HttpSession session) {
        UserDomain users = this.user(request);
        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(password)) {
            return APIResponse.fail("请确认信息输入完整");
        }

        if (!users.getPassword().equals(TaleUtils.MD5encode(users.getUsername() + oldPassword))) {
            return APIResponse.fail("旧密码错误");
        }
        if (password.length() < 6 || password.length() > 14) {
            return APIResponse.fail("请输入6-14位密码");
        }

        try {
            UserDomain temp = new UserDomain();
            temp.setUid(users.getUid());
            String pwd = TaleUtils.MD5encode(users.getUsername() + password);
            temp.setPassword(pwd);
            userService.updateUserInfo(temp);
            logService.addLog(LogActions.UP_PWD.getAction(), null, request.getRemoteAddr(), this.getUid(request));

            //更新session中的数据
            UserDomain original= (UserDomain)session.getAttribute(WebConst.LOGIN_SESSION_KEY);
            original.setPassword(pwd);
            session.setAttribute(WebConst.LOGIN_SESSION_KEY,original);
            return APIResponse.success();
        } catch (Exception e){
            String msg = "密码修改失败";
            if (e instanceof BusinessException) {
                msg = e.getMessage();
            } else {
                LOGGER.error(msg, e);
            }
            return APIResponse.fail(msg);
        }
    }




}

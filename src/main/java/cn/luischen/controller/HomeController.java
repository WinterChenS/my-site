package cn.luischen.controller;

import cn.luischen.constant.ErrorConstant;
import cn.luischen.constant.Types;
import cn.luischen.constant.WebConst;
import cn.luischen.dto.ArchiveDto;
import cn.luischen.dto.cond.ContentCond;
import cn.luischen.exception.BusinessException;
import cn.luischen.model.CommentDomain;
import cn.luischen.model.ContentDomain;
import cn.luischen.service.comment.CommentService;
import cn.luischen.service.content.ContentService;
import cn.luischen.service.meta.MetaService;
import cn.luischen.service.option.OptionService;
import cn.luischen.service.site.SiteService;
import cn.luischen.utils.*;
import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * 首页和关于我的页面控制器
 * Created by Donghua.Chen on 2018/4/28.
 */
@Api("网站首页和关于页面")
@Controller
public class HomeController extends BaseController{

    @Autowired
    private ContentService contentService;

    @Autowired
    private MetaService metaService;

    @Autowired
    private SiteService siteService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private OptionService optionService;



    @ApiIgnore
    @GetMapping(value = {"/about", "/about/index"})
    public String getAbout(HttpServletRequest request){
        this.blogBaseData(request, null);//获取友链
        request.setAttribute("active","about");
        return "site/about";
    }



    @ApiOperation("blog首页")
    @GetMapping(value = {"/blog/","/blog/index"})
    public String blogIndex(
            HttpServletRequest request,
            @ApiParam(name = "limit", value = "页数", required = false)
            @RequestParam(name = "limit", required = false, defaultValue = "11")
                    int limit
    ){
        return this.blogIndex(request, 1, limit);
    }

    @ApiOperation("blog首页-分页")
    @GetMapping(value = "/blog/page/{p}")
    public String blogIndex(
            HttpServletRequest request,
            @PathVariable("p")
                    int p,
            @RequestParam(value = "limit", required = false, defaultValue = "11")
                    int limit
    ){
        p = p < 0 || p > WebConst.MAX_PAGE ? 1 : p;
        ContentCond contentCond = new ContentCond();
        contentCond.setType(Types.ARTICLE.getType());
        PageInfo<ContentDomain> articles = contentService.getArticlesByCond(contentCond, p, limit);
        request.setAttribute("articles", articles);//文章列表
        request.setAttribute("type", "articles");
        request.setAttribute("active", "blog");
//        this.blogBaseData(request, contentCond);//获取公共分类标签等数据
        return "site/blog";
    }

    @ApiOperation("文章内容页")
    @GetMapping(value = "/blog/article/{cid}")
    public String post(
            @ApiParam(name = "cid", value = "文章主键", required = true)
            @PathVariable("cid")
                    Integer cid,
            HttpServletRequest request
    ){
        ContentDomain atricle = contentService.getArticleById(cid);
        request.setAttribute("article", atricle);
        ContentCond contentCond = new ContentCond();
        contentCond.setType(Types.ARTICLE.getType());
//        this.blogBaseData(request, contentCond);//获取公共分类标签等数据
        //更新文章的点击量
        this.updateArticleHit(atricle.getCid(),atricle.getHits());
        List<CommentDomain> commentsPaginator = commentService.getCommentsByCId(cid);
        request.setAttribute("comments", commentsPaginator);
        request.setAttribute("active","blog");
        return "site/blog-details";

    }
    /**
     * 更新文章的点击率
     *
     * @param cid
     * @param chits
     */
    private void updateArticleHit(Integer cid, Integer chits) {
        Integer hits = cache.hget("article", "hits");
        if (chits == null) {
            chits = 0;
        }
        hits = null == hits ? 1 : hits + 1;
        if (hits >= WebConst.HIT_EXCEED) {
            ContentDomain temp = new ContentDomain();
            temp.setCid(cid);
            temp.setHits(chits + hits);
            contentService.updateContentByCid(temp);
            cache.hset("article", "hits", 1);
        } else {
            cache.hset("article", "hits", hits);
        }
    }



    @ApiOperation("归档页-按日期")
    @GetMapping(value = "/blog/archives/{date}")
    public String archives(
            @ApiParam(name = "date", value = "归档日期", required = true)
            @PathVariable("date")
                    String date,
            HttpServletRequest request
    ){
        ContentCond contentCond = new ContentCond();
        Date sd = DateKit.dateFormat(date, "yyyy年MM月");
        int start = DateKit.getUnixTimeByDate(sd);
        int end = DateKit.getUnixTimeByDate(DateKit.dateAdd(DateKit.INTERVAL_MONTH, sd, 1)) - 1;
        contentCond.setStartTime(start);
        contentCond.setEndTime(end);
        contentCond.setType(Types.ARTICLE.getType());
        List<ArchiveDto> archives = siteService.getArchives(contentCond);
        request.setAttribute("archives_list",archives);
//        this.blogBaseData(request, contentCond);//获取公共分类标签等数据
        return "blog/archives";
    }




    @ApiOperation("归档页-按年份")
    @GetMapping(value = "/blog/archives/year/{year}")
    public String archivesAtYear(
            @ApiParam(name = "year", value = "归档日期", required = true)
            @PathVariable("year")
                    String year,
            HttpServletRequest request
    ){
        ContentCond contentCond = new ContentCond();
        int start = DateKit.getUnixTimeByDate(DateKit.getYearStartDay(year, "yyyy"));
        int end = DateKit.getUnixTimeByDate(DateKit.getYearEndDay(year, "yyyy"));
        contentCond.setStartTime(start);
        contentCond.setEndTime(end);
        contentCond.setType(Types.ARTICLE.getType());
        List<ArchiveDto> archives = siteService.getArchives(contentCond);
        request.setAttribute("archives_list",archives);
//        this.blogBaseData(request, contentCond);//获取公共分类标签等数据
        return "blog/archives";
    }





    @ApiOperation("归档页")
    @GetMapping(value = {"/blog/archives", "/blog/archives/index"})
    public String archives(HttpServletRequest request){
        ContentCond contentCond = new ContentCond();
        contentCond.setType(Types.ARTICLE.getType());
        List<ArchiveDto> archives = siteService.getArchives(contentCond);
        request.setAttribute("archives_list",archives);
//        this.blogBaseData(request,contentCond);//获取公共分类标签等数据
        return "blog/archives";
    }


    @ApiOperation("分类")
    @GetMapping(value = "/blog/categories/{category}")
    public String categories(
            @ApiParam(name = "category", value = "分类名", required = true)
            @PathVariable("category")
                    String category,
            HttpServletRequest request
    ){
        return  this.categories(category, 1, 10, request);
    }

    @ApiOperation("分类-分页")
    @GetMapping(value = "/blog/categories/{category}/page/{page}")
    public String categories(
            @ApiParam(name = "category", value = "分类名", required = true)
            @PathVariable("category")
                    String category,
            @ApiParam(name = "page", value = "页数", required = true)
            @PathVariable("page")
                    int page,
            @ApiParam(name = "limit", value = "条数", required = true)
            @RequestParam(name = "limit", required = false, defaultValue = "10")
                    int limit,
            HttpServletRequest request
    ){
        ContentCond contentCond = new ContentCond();
        contentCond.setType(Types.ARTICLE.getType());
        contentCond.setCategory(category);
        PageInfo<ContentDomain> articles = contentService.getArticlesByCond(contentCond, page, limit);
//        this.blogBaseData(request,contentCond);//获取公共分类标签等数据
        request.setAttribute("articles_list", articles);
        request.setAttribute("type", "categories");
        request.setAttribute("param_name", category);
        return "blog/categories";

    }

    @ApiOperation("标签页")
    @GetMapping(value = "/blog/tag/{tag}")
    public String tags(
            @ApiParam(name = "tag", value = "标签名", required = true)
            @PathVariable("tag")
                    String tag,
            HttpServletRequest request
    ){
        return this.tags(tag, 1, 10, request);
    }

    @ApiOperation("标签页-分页")
    @GetMapping(value = "/blog/tag/{tag}/page/{page}")
    public String tags(
            @ApiParam(name = "tag", value = "标签名", required = true)
            @PathVariable("tag")
                    String tag,
            @ApiParam(name = "page", value = "页数", required = true)
            @PathVariable("page")
                    int page,
            @ApiParam(name = "limit", value = "条数", required = false)
            @RequestParam(name = "limit", required = false, defaultValue = "10")
                    int limit,
            HttpServletRequest request
    ){
        ContentCond contentCond = new ContentCond();
        contentCond.setTag(tag);
        contentCond.setType(Types.ARTICLE.getType());
        PageInfo<ContentDomain> articles = contentService.getArticlesByCond(contentCond, page, limit);
//        this.blogBaseData(request,contentCond);//获取公共分类标签等数据
        request.setAttribute("articles_list", articles);
        request.setAttribute("type", "tag");
        request.setAttribute("param_name", tag);
        return "blog/categories";
    }

    @ApiOperation("搜索文章")
    @GetMapping("/blog/search")
    public String search(
            @ApiParam(name = "param", value = "搜索的文字", required = true)
            @RequestParam(name = "param", required = true)
                    String param,
            HttpServletRequest request
    ){
        return this.search(param, 1, 10, request);
    }

    @ApiOperation("搜索文章-分页")
    @GetMapping(value = "/blog/search/{param}/page/{page}")
    public String search(
            @ApiParam(name = "param", value = "搜索的文字", required = true)
            @PathVariable("param")
                    String param,
            @ApiParam(name = "page", value = "页数", required = true)
            @PathVariable("page")
                    int page,
            @ApiParam(name = "limit", value = "条数", required = false)
            @RequestParam(name = "limit", required = false, defaultValue = "10")
                    int limit,
            HttpServletRequest request
    ){
        PageInfo<ContentDomain> pageInfo = contentService.searchArticle(param, page, limit);
        ContentCond contentCond = new ContentCond();
        contentCond.setType(Types.ARTICLE.getType());
//        this.blogBaseData(request,contentCond);//获取公共分类标签等数据
        request.setAttribute("articles", pageInfo);
        request.setAttribute("type", "search");
        request.setAttribute("param_name", param);
        return "blog/index";
    }

    /**
     * 评论操作
     */
    @PostMapping(value = "/blog/comment")
    @ResponseBody
    public APIResponse comment(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam(name = "cid", required = true) Integer cid,
                               @RequestParam(name = "coid", required = false) Integer coid,
                               @RequestParam(name = "author", required = false) String author,
                               @RequestParam(name = "mail", required = false) String mail,
                               @RequestParam(name = "url", required = false) String url,
                               @RequestParam(name = "text", required = true) String text,
                               @RequestParam(name = "_csrf_token", required = true) String _csrf_token) {

        String ref = request.getHeader("Referer");
        if (StringUtils.isBlank(ref) || StringUtils.isBlank(_csrf_token)) {
            return APIResponse.fail("访问失败");
        }

        String token = cache.hget(Types.CSRF_TOKEN.getType(), _csrf_token);
        if (StringUtils.isBlank(token)) {
            return APIResponse.fail("访问失败");
        }

        if (null == cid || StringUtils.isBlank(text)) {
            return APIResponse.fail("请输入完整后评论");
        }

        if (StringUtils.isNotBlank(author) && author.length() > 50) {
            return APIResponse.fail("姓名过长");
        }

        if (StringUtils.isNotBlank(mail) && !TaleUtils.isEmail(mail)) {
            return APIResponse.fail("请输入正确的邮箱格式");
        }

        if (StringUtils.isNotBlank(url) && !PatternKit.isURL(url)) {
            return APIResponse.fail("请输入正确的URL格式");
        }

        if (text.length() > 200) {
            return APIResponse.fail("请输入200个字符以内的评论");
        }

        String val = IPKit.getIpAddrByRequest(request) + ":" + cid;
        Integer count = cache.hget(Types.COMMENTS_FREQUENCY.getType(), val);
        if (null != count && count > 0) {
            return APIResponse.fail("您发表评论太快了，请过会再试");
        }

        author = TaleUtils.cleanXSS(author);
        text = TaleUtils.cleanXSS(text);

        author = EmojiParser.parseToAliases(author);
        text = EmojiParser.parseToAliases(text);

        CommentDomain comments = new CommentDomain();
        comments.setAuthor(author);
        comments.setCid(cid);
        comments.setIp(request.getRemoteAddr());
        comments.setUrl(url);
        comments.setContent(text);
        comments.setMail(mail);
        comments.setParent(coid);
        try {
            commentService.addComment(comments);
            cookie("tale_remember_author", URLEncoder.encode(author, "UTF-8"), 7 * 24 * 60 * 60, response);
            cookie("tale_remember_mail", URLEncoder.encode(mail, "UTF-8"), 7 * 24 * 60 * 60, response);
            if (StringUtils.isNotBlank(url)) {
                cookie("tale_remember_url", URLEncoder.encode(url, "UTF-8"), 7 * 24 * 60 * 60, response);
            }
            // 设置对每个文章1分钟可以评论一次
            cache.hset(Types.COMMENTS_FREQUENCY.getType(), val, 1, 60);

            return APIResponse.success();
        } catch (Exception e) {
            throw BusinessException.withErrorCode(ErrorConstant.Comment.ADD_NEW_COMMENT_FAIL);
        }
    }


    /**
     * 注销
     *
     * @param session
     * @param response
     */
    @RequestMapping("/blog/logout")
    public void logout(HttpSession session, HttpServletResponse response) {
        TaleUtils.logout(session, response);
    }



    /**
     * 设置cookie
     *
     * @param name
     * @param value
     * @param maxAge
     * @param response
     */
    private void cookie(String name, String value, int maxAge, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(false);
        response.addCookie(cookie);
    }


    @ApiOperation("作品主页")
    @GetMapping(value = {"", "/index"})
    public String index(HttpServletRequest request, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        return this.index(1, limit, request);
    }

    @ApiOperation("作品主页-分页")
    @GetMapping(value = "/photo/page/{p}")
    public String index(
            @ApiParam(name = "page", value = "页数", required = false)
            @PathVariable(name = "p")
                    int page,
            @ApiParam(name = "limit", value = "条数", required = false)
            @RequestParam(name = "limit", required = false, defaultValue = "9999")
                    int limit,
            HttpServletRequest request
    ){
        page = page < 0 || page > WebConst.MAX_PAGE ? 1 : page;
        ContentCond contentCond = new ContentCond();
        contentCond.setType(Types.PHOTO.getType());
        PageInfo<ContentDomain> articles = contentService.getArticlesByCond(contentCond, page, limit);
        request.setAttribute("archives", articles);
        request.setAttribute("active", "work");
        return "site/index";
    }


    @ApiOperation("作品内容")
    @GetMapping(value = "/photo/article/{cid}")
    public String article(
            @PathVariable("cid")
                    Integer cid,
            HttpServletRequest request
    ){
        ContentDomain article = contentService.getArticleById(cid);
        request.setAttribute("archive", article);
        request.setAttribute("active","work");
        return "site/works-details";
    }






}

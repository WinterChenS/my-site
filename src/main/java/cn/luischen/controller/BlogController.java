package cn.luischen.controller;

import cn.luischen.constant.Types;
import cn.luischen.constant.WebConst;
import cn.luischen.dto.ArchiveDto;
import cn.luischen.dto.MetaDto;
import cn.luischen.dto.cond.ContentCond;
import cn.luischen.dto.cond.MetaCond;
import cn.luischen.model.ContentDomain;
import cn.luischen.service.content.ContentService;
import cn.luischen.service.meta.MetaService;
import cn.luischen.service.site.SiteService;
import cn.luischen.utils.DateKit;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by Donghua.Chen on 2018/5/1.
 */
@Api("blog接口")
@Controller
@RequestMapping("/blog")
public class BlogController extends BaseController {

    @Autowired
    private ContentService contentService;

    @Autowired
    private MetaService metaService;

    @Autowired
    private SiteService siteService;


    @ApiOperation("blog首页")
    @GetMapping(value = {"","index"})
    public String index(
            HttpServletRequest request,
            @ApiParam(name = "limit", value = "页数", required = false)
            @RequestParam(name = "limit", required = false, defaultValue = "10")
            int limit
    ){
        return this.index(request, 1, limit);
    }

    @ApiOperation("blog首页-分页")
    @GetMapping(value = "page/{p}")
    public String index(
            HttpServletRequest request,
            @PathVariable("p")
            int p,
            @RequestParam(value = "limit", required = false, defaultValue = "10")
            int limit
    ){
        p = p < 0 || p > WebConst.MAX_PAGE ? 1 : p;
        ContentCond contentCond = new ContentCond();
        contentCond.setType(Types.ARTICLE.getType());
        PageInfo<ContentDomain> articles = contentService.getArticlesByCond(contentCond, p, limit);
        request.setAttribute("articles", articles);//文章列表
        request.setAttribute("type", "articles");
        this.blogBaseData(request, contentCond);//获取公共分类标签等数据
        return "blog/index";
    }

    @ApiOperation("文章内容页")
    @GetMapping(value = "/article/{cid}")
    public String post(
            @ApiParam(name = "cid", value = "文章主键", required = true)
            @PathVariable("cid")
            Integer cid,
            HttpServletRequest request
    ){
        ContentDomain atricle = contentService.getAtricleById(cid);
        request.setAttribute("article", atricle);
        ContentCond contentCond = new ContentCond();
        contentCond.setType(Types.ARTICLE.getType());
        this.blogBaseData(request, contentCond);//获取公共分类标签等数据
        return "blog/post";

    }

    @ApiOperation("归档页-按日期")
    @GetMapping(value = "/archives/{date}")
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
        this.blogBaseData(request, contentCond);//获取公共分类标签等数据
        return "blog/archives";
    }




    @ApiOperation("归档页-按年份")
    @GetMapping(value = "/archives/year/{year}")
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
        this.blogBaseData(request, contentCond);//获取公共分类标签等数据
        return "blog/archives";
    }





    @ApiOperation("归档页")
    @GetMapping(value = {"/archives", "/archives/index"})
    public String archives(HttpServletRequest request){
                ContentCond contentCond = new ContentCond();
        contentCond.setType(Types.ARTICLE.getType());
        List<ArchiveDto> archives = siteService.getArchives(contentCond);
        request.setAttribute("archives_list",archives);
        this.blogBaseData(request,contentCond);//获取公共分类标签等数据
        return "blog/archives";
    }


    @ApiOperation("分类")
    @GetMapping(value = "/categories/{category}")
    public String categories(
            @ApiParam(name = "category", value = "分类名", required = true)
            @PathVariable("category")
            String category,
            HttpServletRequest request
    ){
        return  this.categories(category, 1, 10, request);
    }

    @ApiOperation("分类-分页")
    @GetMapping(value = "/categories/{category}/page/{page}")
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
        this.blogBaseData(request,contentCond);//获取公共分类标签等数据
        request.setAttribute("articles_list", articles);
        request.setAttribute("type", "categories");
        request.setAttribute("param_name", category);
        return "blog/categories";

    }

    @ApiOperation("标签页")
    @GetMapping(value = "/tag/{tag}")
    public String tags(
            @ApiParam(name = "tag", value = "标签名", required = true)
            @PathVariable("tag")
            String tag,
            HttpServletRequest request
    ){
        return this.tags(tag, 1, 10, request);
    }

    @ApiOperation("标签页-分页")
    @GetMapping(value = "/tag/{tag}/page/{page}")
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
        this.blogBaseData(request,contentCond);//获取公共分类标签等数据
        request.setAttribute("articles_list", articles);
        request.setAttribute("type", "tag");
        request.setAttribute("param_name", tag);
        return "blog/categories";
    }

    @ApiOperation("搜索文章")
    @GetMapping("/search")
    public String search(
            @ApiParam(name = "param", value = "搜索的文字", required = true)
            @RequestParam(name = "param", required = true)
            String param,
            HttpServletRequest request
    ){
        return this.search(param, 1, 10, request);
    }

    @ApiOperation("搜索文章-分页")
    @GetMapping(value = "/search/{param}/page/{page}")
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
        this.blogBaseData(request,contentCond);//获取公共分类标签等数据
        request.setAttribute("articles", pageInfo);
        request.setAttribute("type", "search");
        request.setAttribute("param_name", param);
        return "blog/index";
    }



}

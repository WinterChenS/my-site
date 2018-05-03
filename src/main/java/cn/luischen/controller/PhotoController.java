package cn.luischen.controller;

import cn.luischen.constant.Types;
import cn.luischen.constant.WebConst;
import cn.luischen.dto.cond.ContentCond;
import cn.luischen.model.ContentDomain;
import cn.luischen.service.content.ContentService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Donghua.Chen on 2018/5/1.
 */
@Api("作品")
@Controller
@RequestMapping(value = "/photo")
public class PhotoController extends BaseController {

    @Autowired
    private ContentService contentService;


    @ApiOperation("作品主页")
    @GetMapping(value = {"", "/index"})
    public String index(HttpServletRequest request, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        return this.index(1, limit, request);
    }

    @ApiOperation("作品主页-分页")
    @GetMapping(value = "page/{p}")
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
        if (page > 1) {
            this.title(request, "第" + page + "页");
        }
        return "photo/index";
    }


    @ApiOperation("作品内容")
    @GetMapping(value = "/article/{cid}")
    public String article(
            @PathVariable("cid")
            Integer cid,
            HttpServletRequest request
    ){
        ContentDomain article = contentService.getAtricleById(cid);
        request.setAttribute("archive", article);
        return "photo/article";
    }
}

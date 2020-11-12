package cn.luischen.controller.admin;


import cn.luischen.controller.BaseController;
import cn.luischen.exception.BusinessException;
import cn.luischen.model.NewsDomain;
import cn.luischen.service.news.NewsService;
import cn.luischen.utils.APIResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("新闻接口")
@Controller
@RequestMapping("/admin/news")
public class NewsController extends BaseController {

    private static final Logger LOGGER = LogManager.getLogger(AuthController.class);

    @Autowired
    NewsService newsService;

    @ApiOperation("文章列表")
    @GetMapping("index")
    @ResponseBody
    public APIResponse getNews() {
        List<NewsDomain> news = newsService.getNews();
        return APIResponse.success(news);
    }

    @ApiOperation("添加新闻")
    @PostMapping("save")
    @ResponseBody
    public APIResponse addNews(
            @ApiParam(name = "title", value = "新闻标题", required = true)
            @RequestParam(name = "title", required = true)
            String title,
            @ApiParam(name = "content", value = "新闻内容", required = true)
            @RequestParam(name = "title", required = true)
            String content
    ) {
        try {
            newsService.addNews(title, content);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            String msg = "新闻添加失败";
            if (e instanceof BusinessException) {
                BusinessException ex = (BusinessException) e;
                msg = ex.getErrorCode();
            }
            return APIResponse.fail(msg);
        }
        return APIResponse.success();
    }

    @ApiOperation("添加文章")
    @PostMapping("update")
    @ResponseBody
    public APIResponse updateNewsById(
            @ApiParam(name = "id", value = "新闻ID", required = true)
            @RequestParam(name = "id", required = true)
            Integer id,
            @ApiParam(name = "title", value = "新闻标题", required = true)
            @RequestParam(name = "title", required = true)
            String title,
            @ApiParam(name = "content", value = "新闻内容", required = true)
            @RequestParam(name = "title", required = true)
            String content
    ) {
        try {
            newsService.updateNewsById(id, title, content);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            String msg = "新闻保存失败";
            if (e instanceof BusinessException) {
                BusinessException ex = (BusinessException) e;
                msg = ex.getErrorCode();
            }
            return APIResponse.fail(msg);
        }
        return APIResponse.success();
    }

    @ApiOperation("删除文章")
    @PostMapping("delete")
    @ResponseBody
    public APIResponse deleteNewsById(
            @ApiParam(name = "id", value = "新闻ID", required = true)
            @RequestParam(name = "id", required = true)
            Integer id
    ) {
        try {
            newsService.deleteNewsById(id);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            String msg = "新闻删除失败";
            if (e instanceof BusinessException) {
                BusinessException ex = (BusinessException) e;
                msg = ex.getErrorCode();
            }
            return APIResponse.fail(msg);
        }
        return APIResponse.success();
    }
}

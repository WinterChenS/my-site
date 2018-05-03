package cn.luischen.controller.admin;

import cn.luischen.constant.ErrorConstant;
import cn.luischen.constant.Types;
import cn.luischen.controller.BaseController;
import cn.luischen.dto.cond.MetaCond;
import cn.luischen.exception.BusinessException;
import cn.luischen.model.MetaDomain;
import cn.luischen.service.meta.MetaService;
import cn.luischen.utils.APIResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Donghua.Chen on 2018/5/1.
 */
@Api("友链")
@Controller
@RequestMapping(value = "admin/links")
public class LinksController extends BaseController {


    private static final Logger LOGGER = LoggerFactory.getLogger(LinksController.class);

    @Autowired
    private MetaService metaService;

    @ApiOperation("友链页面")
    @GetMapping(value = "")
    public String index(HttpServletRequest request) {

        MetaCond metaCond = new MetaCond();
        metaCond.setType(Types.LINK.getType());
        List<MetaDomain> metas = metaService.getMetas(metaCond);
        request.setAttribute("links", metas);
        return "admin/links";
    }

    @ApiOperation("新增友链")
    @PostMapping(value = "save")
    @ResponseBody
    public APIResponse addLink(
            @ApiParam(name = "title", value = "标签", required = true)
            @RequestParam(name = "title", required = true)
            String title,
            @ApiParam(name = "url", value = "链接", required = true)
            @RequestParam(name = "url", required = true)
            String url,
            @ApiParam(name = "logo", value = "logo", required = false)
            @RequestParam(name = "logo", required = false)
            String logo,
            @ApiParam(name = "mid", value = "meta编号", required = false)
            @RequestParam(name = "mid", required = false)
            Integer mid,
            @ApiParam(name = "sort", value = "sort", required = false)
            @RequestParam(name = "sort", required = false, defaultValue = "0")
            int sort
    ){
        try {
            MetaDomain meta = new MetaDomain();
            meta.setName(title);
            meta.setSlug(url);
            meta.setDescription(logo);
            meta.setSort(sort);
            meta.setType(Types.LINK.getType());
            if (null != mid){
                meta.setMid(mid);
                metaService.updateMeta(meta);
            }else {
                metaService.addMeta(meta);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw BusinessException.withErrorCode(ErrorConstant.Meta.ADD_META_FAIL);
        }
        return APIResponse.success();
    }


    @ApiOperation("删除友链")
    @PostMapping(value = "delete")
    @ResponseBody
    public APIResponse delete(
            @ApiParam(name = "mid", value = "meta主键", required = true)
            @RequestParam(name = "mid", required = true)
                    int mid
    ) {
        try {
            metaService.deleteMetaById(mid);
        } catch (Exception e) {
            e.printStackTrace();
            throw BusinessException.withErrorCode(ErrorConstant.Meta.ADD_META_FAIL);
        }
        return APIResponse.success();

    }
}

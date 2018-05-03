package cn.luischen.controller;

import cn.luischen.constant.WebConst;
import cn.luischen.dao.OptionDao;
import cn.luischen.model.OptionsDomain;
import cn.luischen.service.option.OptionService;
import cn.luischen.utils.Commons;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页和关于我的页面控制器
 * Created by Donghua.Chen on 2018/4/28.
 */
@Api("网站首页和关于页面")
@Controller
public class HomeController extends BaseController{

    @Autowired
    private OptionService optionService;

    @ApiIgnore
    @GetMapping("/")
    public String getIndex(HttpServletRequest request){
        return "home/index";
    }



    @ApiIgnore
    @GetMapping("/about")
    public String getAbout(HttpServletRequest request){
        return "home/about";
    }










}

package cn.luischen.interceptor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 向MVC中添加自定义组件
 * Created by Donghua.Chen on 2018/4/30.
 */
@Component
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private BaseInterceptor baseInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(baseInterceptor);
    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/admin/**")
//                .addResourceLocations("/public", "classpath:/admin/")
//                .setCachePeriod(31556926);
//    }

}

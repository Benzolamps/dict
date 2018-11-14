package com.benzolamps.dict.controller.interceptor;

import com.benzolamps.dict.controller.ErrorController;
import com.benzolamps.dict.controller.IndexController;
import com.benzolamps.dict.controller.util.Constant;
import com.benzolamps.dict.util.DictWeb;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * ContentType拦截器
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-11 18:53:34
 */
@Interceptor
public class ContentTypeInterceptor extends BaseInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader("Cache-Control", "no-cache, must-revalidate");
        response.setHeader("Access-Control-Allow-Origin", "*");
        Method method = ((HandlerMethod) handler).getMethod();
        if (!method.getDeclaringClass().equals(ErrorController.class)) {
            String contentType;
            if (("index".equals(method.getName()) || "ping".equals(method.getName())) && IndexController.class.equals(method.getDeclaringClass())) {
                contentType = Constant.HTML;
            } else {
                String url = request.getRequestURL().toString();
                url = url.split("\\?")[0];
                int index = url.lastIndexOf(".") + 1;
                Assert.state(index >= 0, "没有扩展名");
                url = url.substring(index);
                contentType = DictWeb.convertContentType(url);
            }
            response.setContentType(contentType);
        }
        return super.preHandle(request, response, handler);
    }
}

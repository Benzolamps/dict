package com.benzolamps.dict.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;

/**
 * 主页Controller
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-7 23:48:57
 */
@RestController
public class IndexController extends BaseController {

    @Value("${system.remote_base_url}")
    private String remoteBaseUrl;

    /**
     * 主页
     * @param suffix 主页后缀
     * @return ModelAndView
     */
    @GetMapping("/index.{suffix}")
    protected ModelAndView index(@PathVariable String suffix) {
        return new ModelAndView("html".equals(suffix) ? "view/default/index" : "redirect:index.html");
    }

    /**
     * 主页
     * @return ModelAndView
     */
    @GetMapping({"", "/"})
    protected ModelAndView index() {
        return new ModelAndView("redirect:index.html");
    }

    /**
     * 可解析资源路径
     * @param request request
     * @return ModelAndView
     */
    @RequestMapping(value = "/res/**", method = {RequestMethod.GET, RequestMethod.POST})
    protected ModelAndView res(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        int index = url.indexOf("res");
        return new ModelAndView(url.substring(index));
    }

    /**
     * 远程资源路径
     * @param request request
     * @return ModelAndView
     */
    @GetMapping("/remote/**")
    protected ResponseEntity<UrlResource> remote(HttpServletRequest request) throws MalformedURLException {
        String url = request.getRequestURL().toString();
        int index = url.indexOf("remote") + 6;
        return ResponseEntity.ok().body(new UrlResource(remoteBaseUrl + url.substring(index)));
    }
}

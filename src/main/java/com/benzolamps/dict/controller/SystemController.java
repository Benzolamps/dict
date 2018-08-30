package com.benzolamps.dict.controller;

import com.benzolamps.dict.controller.interceptor.NavigationView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 系统管理Controller
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-4 19:21:16
 */
@RestController
@RequestMapping("system/")
public class SystemController extends BaseController {

    /**
     * 系统信息界面
     * @return ModelAndView
     */
    @NavigationView
    @GetMapping("/info.html")
    protected ModelAndView info() {
        return new ModelAndView("view/system/info");
    }

    /**
     * 关于我们界面
     * @return ModelAndView
     */
    @NavigationView
    @GetMapping("/about.html")
    protected ModelAndView about() {
        return new ModelAndView("view/system/about");
    }

    /**
     * 系统设置界面
     * @return ModelAndView
     */
    @NavigationView
    @GetMapping("/settings.html")
    protected ModelAndView settings() {
        return new ModelAndView("view/system/settings");
    }
}

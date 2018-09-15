package com.benzolamps.dict.controller;

import com.benzolamps.dict.controller.interceptor.NavigationView;
import com.benzolamps.dict.controller.vo.BaseVo;
import com.benzolamps.dict.service.base.MiscellaneousService;
import com.benzolamps.dict.util.DictLambda;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * 系统管理Controller
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-4 19:21:16
 */
@RestController
@RequestMapping("system/")
public class SystemController extends BaseController {

    @Resource
    private MiscellaneousService miscellaneousService;

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

    /**
     * 清理缓存界面
     * @return ModelAndView
     */
    @RequestMapping(value = "/clean.html", method = {RequestMethod.GET, RequestMethod.POST})
    protected ModelAndView clean() {
        ModelAndView mv = new ModelAndView("view/system/clean");
        mv.addObject("size", miscellaneousService.databaseFileSize());
        return mv;
    }

    /**
     * clean
     * @return ModelAndView
     */
    @ResponseBody
    @GetMapping("/clean.json")
    protected BaseVo cleanProcess() {
        miscellaneousService.clean();
        return SUCCESS_VO;
    }

    /**
     * 关闭系统服务界面
     * @return ModelAndView
     */
    @RequestMapping(value = "/shutdown.html", method = {RequestMethod.GET, RequestMethod.POST})
    protected ModelAndView shutdown() {
        return new ModelAndView("view/system/shutdown");
    }

    /**
     * 关闭系统服务
     * @return 关闭成功
     */
    @ResponseBody
    @PostMapping("shutdown.json")
    protected BaseVo shutdownProcess() {
        new Thread((DictLambda.Action) () -> {
            Thread.sleep(100);
            System.exit(0);
        }).start();
        return SUCCESS_VO;
    }
}

package com.benzolamps.dict.controller;

import com.benzolamps.dict.service.base.ShuffleSolutionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * 乱序方案控制器
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-26 16:13:45
 */
@RestController
@RequestMapping("shuffle_solution/")
public class ShuffleSolutionController extends BaseController {

    @Resource
    private ShuffleSolutionService shuffleSolutionService;

    /**
     * 列出所有乱序方案
     * @return ModelAndView
     */
    @RequestMapping(value = "/list.html", method = {RequestMethod.GET, RequestMethod.POST})
    protected ModelAndView list() {
        ModelAndView mv = new ModelAndView("view/shuffle_solution/list");
        mv.addObject("page", shuffleSolutionService.getSolutions());
        return mv;
    }
}

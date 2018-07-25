package com.benzolamps.dict.controller;

import com.benzolamps.dict.controller.interceptor.WindowView;
import com.benzolamps.dict.service.base.ShuffleSolutionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@RestController
@RequestMapping("shuffle_solution/")
public class ShuffleSolutionController extends BaseController {

    @Resource
    private ShuffleSolutionService shuffleSolutionService;

    @RequestMapping("/list.html")
    protected ModelAndView list() {
        ModelAndView mv = new ModelAndView("view/shuffle_solution/list");
        mv.addObject("page", shuffleSolutionService.getSolutions());
        return mv;
    }

    @RequestMapping("/add.html")
    @WindowView
    protected ModelAndView add() {
        ModelAndView mv = new ModelAndView("view/shuffle_solution/add");
        System.out.println(shuffleSolutionService.getAvailableCopyStrategyNames());
        mv.addObject("strategies", shuffleSolutionService.getAvailableCopyStrategyNames());
        return mv;
    }
}

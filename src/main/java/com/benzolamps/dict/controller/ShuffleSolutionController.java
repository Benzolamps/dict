package com.benzolamps.dict.controller;

import com.benzolamps.dict.bean.ShuffleSolution;
import com.benzolamps.dict.controller.interceptor.WindowView;
import com.benzolamps.dict.controller.vo.DataVo;
import com.benzolamps.dict.service.base.ShuffleSolutionService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
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
@Slf4j
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

    /**
     * 添加乱序方案界面
     * @return ModelAndView
     */
    @RequestMapping("/add.html")
    @WindowView
    protected ModelAndView add() {
        ModelAndView mv = new ModelAndView("view/shuffle_solution/add");
        System.out.println(shuffleSolutionService.getAvailableCopyStrategyNames());
        mv.addObject("strategies", shuffleSolutionService.getAvailableCopyStrategyNames());
        return mv;
    }

    /**
     * 保存乱序方案
     * @param shuffleSolution 乱序方案
     * @return 保存后的乱序方案
     */
    @PostMapping("/save.json")
    @ResponseBody
    protected DataVo save(@RequestBody ShuffleSolution shuffleSolution) {

        try {
            shuffleSolution = shuffleSolutionService.persist(shuffleSolution);
            return new DataVo(shuffleSolution);
        } catch (Throwable e) {
            return new DataVo(e.getMessage(), (byte) 1);
        }
    }

    /**
     * 获取乱序策略的信息
     * @param className 乱序策略类名
     * @return vo
     */
    @PostMapping("/property_info.json")
    @ResponseBody
    protected DataVo propertyInfo(String className) {
        return wrapperData(shuffleSolutionService.getShuffleSolutionPropertyInfo(className));
    }
}
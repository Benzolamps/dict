package com.benzolamps.dict.controller;

import com.benzolamps.dict.bean.ShuffleSolution;
import com.benzolamps.dict.controller.interceptor.WindowView;
import com.benzolamps.dict.controller.vo.BaseVo;
import com.benzolamps.dict.controller.vo.DataVo;
import com.benzolamps.dict.service.base.ShuffleSolutionService;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * 乱序方案Controller
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
        mv.addObject("page", shuffleSolutionService.findAll());
        return mv;
    }

    /**
     * 添加乱序方案界面
     * @return ModelAndView
     */
    @RequestMapping(value = "/add.html", method = {RequestMethod.GET, RequestMethod.POST})
    @WindowView
    protected ModelAndView add() {
        ModelAndView mv = new ModelAndView("view/shuffle_solution/add");
        mv.addObject("strategies", shuffleSolutionService.getAvailableStrategyNames());
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
        Assert.isTrue(shuffleSolutionService.isSpare(), "最多只能添加 10 个乱序方案");
        shuffleSolution = shuffleSolutionService.persist(shuffleSolution);
        return new DataVo(shuffleSolution);
    }

    /**
     * 修改乱序方案界面
     * @param id id
     * @return ModelAndView
     */
    @RequestMapping(value = "/edit.html", method = {RequestMethod.GET, RequestMethod.POST})
    @WindowView
    protected ModelAndView edit(Integer id) {
        ModelAndView mv = new ModelAndView("view/shuffle_solution/edit");
        mv.addObject("solution", shuffleSolutionService.find(id));
        mv.addObject("strategies", shuffleSolutionService.getAvailableStrategyNames());
        return mv;
    }

    /**
     * 更新乱序方案
     * @param shuffleSolution 乱序方案
     * @return 保存后的乱序方案
     */
    @PostMapping("/update.json")
    @ResponseBody
    protected DataVo update(@RequestBody ShuffleSolution shuffleSolution) throws ClassNotFoundException {
        shuffleSolution = shuffleSolutionService.update(shuffleSolution);
        return wrapperData(shuffleSolution);
    }

    /**
     * 删除乱序方案
     * @return 提示信息
     */
    @PostMapping("/delete.json")
    @ResponseBody
    protected BaseVo delete(@RequestParam("id") Integer... ids) {
        Arrays.stream(ids).forEach(shuffleSolutionService::remove);
        return SUCCESS_VO;
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
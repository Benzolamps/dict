package com.benzolamps.dict.controller;

import com.benzolamps.dict.bean.Clazz;
import com.benzolamps.dict.controller.interceptor.NavigationView;
import com.benzolamps.dict.controller.interceptor.WindowView;
import com.benzolamps.dict.controller.vo.BaseVo;
import com.benzolamps.dict.controller.vo.DataVo;
import com.benzolamps.dict.dao.core.Page;
import com.benzolamps.dict.dao.core.Pageable;
import com.benzolamps.dict.service.base.ClazzService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * 班级Controller
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-19 22:09:18
 */
@RestController
@RequestMapping("clazz/")
public class ClazzController extends BaseController {

    @Resource
    private ClazzService clazzService;

    /**
     * 列出所有班级
     * @return ModelAndView
     */
    @RequestMapping(value = "/list.html", method = {RequestMethod.GET, RequestMethod.POST})
    @NavigationView
    protected ModelAndView list(@RequestBody(required = false) Pageable pageable) {
        ModelAndView mv = new ModelAndView("view/clazz/list");
        Page<Clazz> libraries = clazzService.findPage(pageable);
        mv.addObject("page", libraries);
        return mv;
    }

    /**
     * 添加班级
     * @return ModelAndView
     */
    @RequestMapping(value = "/add.html", method = {RequestMethod.GET, RequestMethod.POST})
    @WindowView
    protected ModelAndView add() {
        return new ModelAndView("view/clazz/add");
    }

    /**
     * 保存短语
     * @param clazz 班级
     * @return 保存后的班级
     */
    @PostMapping("/save.json")
    @ResponseBody
    protected DataVo save(@RequestBody Clazz clazz) {
        clazz = clazzService.persist(clazz);
        return wrapperData(clazz);
    }

    /**
     * 修改班级
     * @param id id
     * @return ModelAndView
     */
    @RequestMapping(value = "/edit.html", method = {RequestMethod.GET, RequestMethod.POST})
    @WindowView
    protected ModelAndView edit(Integer id) {
        ModelAndView mv = new ModelAndView("view/clazz/edit");
        mv.addObject("clazz", clazzService.find(id));
        return mv;
    }

    /**
     * 更新班级
     * @param clazz 班级
     * @return 更新后的班级
     */
    @PostMapping("/update.json")
    @ResponseBody
    protected DataVo update(@RequestBody Clazz clazz) {
        clazz = clazzService.update(clazz);
        return wrapperData(clazz);
    }

    /**
     * 删除班级
     * @return 提示信息
     */
    @PostMapping("/delete.json")
    @ResponseBody
    protected BaseVo delete(@RequestParam("id") Integer... ids) {
        clazzService.remove(ids);
        return SUCCESS_VO;
    }
}

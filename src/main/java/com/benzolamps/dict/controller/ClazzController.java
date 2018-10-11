package com.benzolamps.dict.controller;

import com.benzolamps.dict.bean.Clazz;
import com.benzolamps.dict.controller.interceptor.NavigationView;
import com.benzolamps.dict.controller.interceptor.WindowView;
import com.benzolamps.dict.controller.vo.BaseVo;
import com.benzolamps.dict.controller.vo.DataVo;
import com.benzolamps.dict.dao.core.Filter;
import com.benzolamps.dict.dao.core.Page;
import com.benzolamps.dict.dao.core.Pageable;
import com.benzolamps.dict.service.base.ClazzService;
import com.benzolamps.dict.service.base.PhraseGroupService;
import com.benzolamps.dict.service.base.WordGroupService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

import java.util.Arrays;

import static com.benzolamps.dict.bean.Group.Status.NORMAL;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 班级Controller
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-19 22:09:18
 */
@RestController
@RequestMapping("clazz")
public class ClazzController extends BaseController {

    @Resource
    private ClazzService clazzService;

    @Resource
    private WordGroupService wordGroupService;

    @Resource
    private PhraseGroupService phraseGroupService;

    /**
     * 列出所有班级
     * @return ModelAndView
     */
    @RequestMapping(value = "list.html", method = {GET, POST})
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
    @RequestMapping(value = "add.html", method = {GET, POST})
    @WindowView
    protected ModelAndView add() {
        return new ModelAndView("view/clazz/add");
    }

    /**
     * 保存短语
     * @param clazz 班级
     * @return 保存后的班级
     */
    @PostMapping("save.json")
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
    @RequestMapping(value = "edit.html", method = {GET, POST})
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
    @PostMapping("update.json")
    @ResponseBody
    protected DataVo update(@RequestBody Clazz clazz) {
        clazz = clazzService.update(clazz);
        return wrapperData(clazz);
    }

    /**
     * 删除班级
     * @return 提示信息
     */
    @PostMapping("delete.json")
    @ResponseBody
    protected BaseVo delete(@RequestParam("id") Integer... ids) {
        clazzService.remove(ids);
        return SUCCESS_VO;
    }

    /**
     * 添加到单词分组
     * @param clazzIds 班级id
     * @return ModelAndView
     */
    @WindowView
    @GetMapping("add_to_word_group.html")
    protected ModelAndView addToWordGroup(@RequestParam("clazzId") Integer... clazzIds) {
        ModelAndView mv = new ModelAndView("view/clazz/add_to_word_group");
        mv.addObject("groups", wordGroupService.findList(Filter.eq("status", NORMAL)));
        mv.addObject("clazzes", clazzService.findList(Filter.in("id", Arrays.asList(clazzIds))));
        return mv;
    }

    /**
     * 添加到短语分组
     * @param clazzIds 班级id
     * @return ModelAndView
     */
    @WindowView
    @GetMapping("add_to_phrase_group.html")
    protected ModelAndView addToPhraseGroup(@RequestParam("clazzId") Integer... clazzIds) {
        ModelAndView mv = new ModelAndView("view/clazz/add_to_phrase_group");
        mv.addObject("groups", phraseGroupService.findList(Filter.eq("status", NORMAL)));
        mv.addObject("clazzes", clazzService.findList(Filter.in("id", Arrays.asList(clazzIds))));
        return mv;
    }
}

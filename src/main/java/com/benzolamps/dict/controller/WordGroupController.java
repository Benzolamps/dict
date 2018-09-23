package com.benzolamps.dict.controller;

import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.controller.interceptor.NavigationView;
import com.benzolamps.dict.controller.interceptor.WindowView;
import com.benzolamps.dict.controller.vo.BaseVo;
import com.benzolamps.dict.controller.vo.DataVo;
import com.benzolamps.dict.dao.core.Pageable;
import com.benzolamps.dict.service.base.WordGroupService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * 单词分组Controller
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-21 22:46:57
 */
@RestController
@RequestMapping("word_group")
public class WordGroupController extends BaseController {
    
    @Resource
    private WordGroupService wordGroupService;

    /**
     * 列出所有单词分组
     * @return ModelAndView
     */
    @RequestMapping(value = "/list.html", method = {RequestMethod.GET, RequestMethod.POST})
    @NavigationView
    protected ModelAndView list(@RequestBody(required = false) Pageable pageable) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("view/word_group/list");
        mv.addObject("page", wordGroupService.findPage(pageable));
        return mv;
    }

    /**
     * 添加单词分组
     * @return ModelAndView
     */
    @RequestMapping(value = "/add.html", method = {RequestMethod.GET, RequestMethod.POST})
    @WindowView
    protected ModelAndView add() {
        return new ModelAndView("view/word_group/add");
    }

    /**
     * 保存单词分组
     * @param wordGroup 单词分组
     * @return 保存后的单词分组
     */
    @PostMapping("/save.json")
    @ResponseBody
    protected DataVo save(@RequestBody Group wordGroup) {
        wordGroup = wordGroupService.persist(wordGroup);
        return wrapperData(wordGroup);
    }

    /**
     * 修改单词分组
     * @param id id
     * @return ModelAndView
     */
    @RequestMapping(value = "/edit.html", method = {RequestMethod.GET, RequestMethod.POST})
    @WindowView
    protected ModelAndView edit(Integer id) {
        ModelAndView mv = new ModelAndView("view/word_group/edit");
        mv.addObject("wordGroup", wordGroupService.find(id));
        return mv;
    }

    /**
     * 更新单词分组
     * @param wordGroup 单词分组
     * @return 更新后的单词分组
     */
    @PostMapping("/update.json")
    @ResponseBody
    protected DataVo update(@RequestBody Group wordGroup) {
        wordGroup = wordGroupService.update(wordGroup, "status");
        return wrapperData(wordGroup);
    }

    /**
     * 删除单词分组
     * @return 提示信息
     */
    @PostMapping("/delete.json")
    @ResponseBody
    protected BaseVo delete(@RequestParam("id") Integer... ids) {
        wordGroupService.remove(ids);
        return SUCCESS_VO;
    }

    /**
     * 检测分组名称是否已存在
     * @param name 名称
     * @return 检测结果
     */
    @RequestMapping(value = "/name_not_exists.json", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    protected boolean nameNotExists(String name) {
        return !wordGroupService.nameExists(name);
    }
}

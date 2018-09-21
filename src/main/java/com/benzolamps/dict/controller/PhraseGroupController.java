package com.benzolamps.dict.controller;

import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.controller.interceptor.NavigationView;
import com.benzolamps.dict.controller.interceptor.WindowView;
import com.benzolamps.dict.controller.vo.BaseVo;
import com.benzolamps.dict.controller.vo.DataVo;
import com.benzolamps.dict.dao.core.Pageable;
import com.benzolamps.dict.service.base.PhraseGroupService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * 短语分组Controller
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-21 22:46:57
 */
@RestController
@RequestMapping("phrase_group")
public class PhraseGroupController extends BaseController {
    
    @Resource
    private PhraseGroupService phraseGroupService;

    /**
     * 列出所有短语分组
     * @return ModelAndView
     */
    @RequestMapping(value = "/list.html", method = {RequestMethod.GET, RequestMethod.POST})
    @NavigationView
    protected ModelAndView list(@RequestBody(required = false) Pageable pageable) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("view/phraseGroup/list");
        mv.addObject("page", phraseGroupService.findPage(pageable));
        return mv;
    }

    /**
     * 添加短语分组
     * @return ModelAndView
     */
    @RequestMapping(value = "/add.html", method = {RequestMethod.GET, RequestMethod.POST})
    @WindowView
    protected ModelAndView add() {
        return new ModelAndView("view/phraseGroup/add");
    }

    /**
     * 保存短语分组
     * @param phraseGroup 短语分组
     * @return 保存后的短语分组
     */
    @PostMapping("/save.json")
    @ResponseBody
    protected DataVo save(@RequestBody Group phraseGroup) {
        phraseGroup = phraseGroupService.persist(phraseGroup);
        return wrapperData(phraseGroup);
    }

    /**
     * 修改短语分组
     * @param id id
     * @return ModelAndView
     */
    @RequestMapping(value = "/edit.html", method = {RequestMethod.GET, RequestMethod.POST})
    @WindowView
    protected ModelAndView edit(Integer id) {
        ModelAndView mv = new ModelAndView("view/phraseGroup/edit");
        mv.addObject("phraseGroup", phraseGroupService.find(id));
        return mv;
    }

    /**
     * 更新短语分组
     * @param phraseGroup 短语分组
     * @return 更新后的短语分组
     */
    @PostMapping("/update.json")
    @ResponseBody
    protected DataVo update(@RequestBody Group phraseGroup) {
        phraseGroup = phraseGroupService.update(phraseGroup);
        return wrapperData(phraseGroup);
    }

    /**
     * 删除短语分组
     * @return 提示信息
     */
    @PostMapping("/delete.json")
    @ResponseBody
    protected BaseVo delete(@RequestParam("id") Integer... ids) {
        phraseGroupService.remove(ids);
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
        return !phraseGroupService.nameExists(name);
    }
}

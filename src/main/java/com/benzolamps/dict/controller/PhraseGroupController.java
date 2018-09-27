package com.benzolamps.dict.controller;

import com.benzolamps.dict.bean.*;
import com.benzolamps.dict.controller.interceptor.NavigationView;
import com.benzolamps.dict.controller.interceptor.WindowView;
import com.benzolamps.dict.controller.vo.BaseVo;
import com.benzolamps.dict.controller.vo.DataVo;
import com.benzolamps.dict.dao.core.Pageable;
import com.benzolamps.dict.service.base.ClazzService;
import com.benzolamps.dict.service.base.PhraseGroupService;
import com.benzolamps.dict.service.base.PhraseService;
import com.benzolamps.dict.service.base.StudentService;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Arrays;

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

    @Resource
    private PhraseService phraseService;

    @Resource
    private StudentService studentService;

    @Resource
    private ClazzService clazzService;

    /**
     * 列出所有短语分组
     * @return ModelAndView
     */
    @RequestMapping(value = "/list.html", method = {RequestMethod.GET, RequestMethod.POST})
    @NavigationView
    protected ModelAndView list(@RequestBody(required = false) Pageable pageable) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("view/phrase_group/list");
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
        return new ModelAndView("view/phrase_group/add");
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
        ModelAndView mv = new ModelAndView("view/phrase_group/edit");
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
        phraseGroup = phraseGroupService.update(phraseGroup, "status");
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

    /**
     * 添加短语
     * @param phraseGroupIds 短语分组id
     * @param phraseIds 短语id
     * @return 添加成功
     */
    @PostMapping(value = "add_phrases.json")
    @ResponseBody
    protected BaseVo addPhrases(@RequestParam("groupId") Integer[] phraseGroupIds, @RequestParam("phraseId") Integer... phraseIds) {
        Assert.notEmpty(phraseGroupIds, "phrase group id不能为null或空");
        Assert.noNullElements(phraseGroupIds, "phrase group id不能存在为null的元素");
        for (Integer phraseGroupId : phraseGroupIds) {
            Group phraseGroup = phraseGroupService.find(phraseGroupId);
            Assert.notNull(phraseGroup, "phrase group不存在");
            Assert.noNullElements(phraseIds, "phrase ids不能存在为null的元素");
            Phrase[] phrases = Arrays.stream(phraseIds).map(phraseService::find).toArray(Phrase[]::new);
            phraseGroupService.addPhrases(phraseGroup, phrases);
        }
        return SUCCESS_VO;
    }

    /**
     * 添加学生
     * @param phraseGroupIds 短语分组id
     * @param studentIds 短语id
     * @return 添加成功
     */
    @PostMapping(value = "add_students.json")
    @ResponseBody
    protected BaseVo addStudents(@RequestParam("groupId") Integer[] phraseGroupIds, @RequestParam("studentId") Integer[] studentIds) {
        Assert.notEmpty(phraseGroupIds, "phrase group id不能为null或空");
        Assert.noNullElements(phraseGroupIds, "phrase group id不能存在为null的元素");
        for (Integer phraseGroupId : phraseGroupIds) {
            Group phraseGroup = phraseGroupService.find(phraseGroupId);
            Assert.notNull(phraseGroup, "phrase group不存在");
            Assert.noNullElements(studentIds, "student ids不能存在为null的元素");
            Student[] students = Arrays.stream(studentIds).map(studentService::find).toArray(Student[]::new);
            phraseGroupService.addStudents(phraseGroup, students);
        }
        return SUCCESS_VO;
    }

    /**
     * 添加班级
     * @param phraseGroupIds 短语分组id
     * @param clazzIds 班级id
     * @return 添加成功
     */
    @PostMapping(value = "add_clazzes.json")
    @ResponseBody
    protected BaseVo addClazzes(@RequestParam("groupId") Integer[] phraseGroupIds, @RequestParam("clazzId") Integer[] clazzIds) {
        Assert.notEmpty(phraseGroupIds, "phrase group id不能为null或空");
        Assert.noNullElements(phraseGroupIds, "phrase group id不能存在为null的元素");
        for (Integer phraseGroupId : phraseGroupIds) {
            Group phraseGroup = phraseGroupService.find(phraseGroupId);
            Assert.notNull(phraseGroup, "phrase group不存在");
            Assert.noNullElements(clazzIds, "clazz ids不能存在为null的元素");
            Clazz[] clazzes = Arrays.stream(clazzIds).map(clazzService::find).toArray(Clazz[]::new);
            phraseGroupService.addClazzes(phraseGroup, clazzes);
        }
        return SUCCESS_VO;
    }
}

package com.benzolamps.dict.controller;

import com.benzolamps.dict.bean.Clazz;
import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.bean.Word;
import com.benzolamps.dict.controller.interceptor.NavigationView;
import com.benzolamps.dict.controller.interceptor.WindowView;
import com.benzolamps.dict.controller.vo.BaseVo;
import com.benzolamps.dict.controller.vo.DataVo;
import com.benzolamps.dict.dao.core.Pageable;
import com.benzolamps.dict.service.base.ClazzService;
import com.benzolamps.dict.service.base.StudentService;
import com.benzolamps.dict.service.base.WordGroupService;
import com.benzolamps.dict.service.base.WordService;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Arrays;

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

    @Resource
    private WordService wordService;

    @Resource
    private StudentService studentService;

    @Resource
    private ClazzService clazzService;

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
    protected boolean nameNotExists(@RequestParam String name) {
        return !wordGroupService.nameExists(name);
    }

    /**
     * 添加单词
     * @param wordGroupIds 单词分组id
     * @param wordIds 单词id
     * @return 添加成功
     */
    @PostMapping(value = "add_words.json")
    @ResponseBody
    protected BaseVo addWords(@RequestParam("groupId") Integer[] wordGroupIds, @RequestParam("wordId") Integer... wordIds) {
        Assert.notEmpty(wordGroupIds, "word group id不能为null或空");
        Assert.noNullElements(wordGroupIds, "word group id不能存在为null的元素");
        for (Integer wordGroupId : wordGroupIds) {
            Group wordGroup = wordGroupService.find(wordGroupId);
            Assert.notNull(wordGroup, "word group不存在");
            Assert.noNullElements(wordIds, "word ids不能存在为null的元素");
            Word[] words = Arrays.stream(wordIds).map(wordService::find).toArray(Word[]::new);
            wordGroupService.addWords(wordGroup, words);
        }
        return SUCCESS_VO;
    }

    /**
     * 添加学生
     * @param wordGroupIds 单词分组id
     * @param studentIds 单词id
     * @return 添加成功
     */
    @PostMapping(value = "add_students.json")
    @ResponseBody
    protected BaseVo addStudents(@RequestParam("groupId") Integer[] wordGroupIds, @RequestParam("studentId") Integer[] studentIds) {
        Assert.notEmpty(wordGroupIds, "word group id不能为null或空");
        Assert.noNullElements(wordGroupIds, "word group id不能存在为null的元素");
        for (Integer wordGroupId : wordGroupIds) {
            Group wordGroup = wordGroupService.find(wordGroupId);
            Assert.notNull(wordGroup, "word group不存在");
            Assert.noNullElements(studentIds, "student ids不能存在为null的元素");
            Student[] students = Arrays.stream(studentIds).map(studentService::find).toArray(Student[]::new);
            wordGroupService.addStudents(wordGroup, students);
        }
        return SUCCESS_VO;
    }

    /**
     * 添加班级
     * @param wordGroupIds 单词分组id
     * @param clazzIds 班级id
     * @return 添加成功
     */
    @PostMapping(value = "add_clazzes.json")
    @ResponseBody
    protected BaseVo addClazzes(@RequestParam("groupId") Integer[] wordGroupIds, @RequestParam("clazzId") Integer[] clazzIds) {
        Assert.notEmpty(wordGroupIds, "word group id不能为null或空");
        Assert.noNullElements(wordGroupIds, "word group id不能存在为null的元素");
        for (Integer wordGroupId : wordGroupIds) {
            Group wordGroup = wordGroupService.find(wordGroupId);
            Assert.notNull(wordGroup, "word group不存在");
            Assert.noNullElements(clazzIds, "clazz ids不能存在为null的元素");
            Clazz[] clazzes = Arrays.stream(clazzIds).map(clazzService::find).toArray(Clazz[]::new);
            wordGroupService.addClazzes(wordGroup, clazzes);
        }
        return SUCCESS_VO;
    }
}

package com.benzolamps.dict.controller;

import com.benzolamps.dict.bean.Clazz;
import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.bean.Word;
import com.benzolamps.dict.controller.interceptor.NavigationView;
import com.benzolamps.dict.controller.interceptor.WindowView;
import com.benzolamps.dict.controller.vo.BaseVo;
import com.benzolamps.dict.controller.vo.ClazzStudentTreeVo;
import com.benzolamps.dict.controller.vo.DataVo;
import com.benzolamps.dict.dao.core.Pageable;
import com.benzolamps.dict.service.base.*;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.*;

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

    @Resource
    private LibraryService libraryService;

    /**
     * 列出所有单词分组
     * @return ModelAndView
     */
    @RequestMapping(value = "/list.html", method = {RequestMethod.GET, RequestMethod.POST})
    @NavigationView
    protected ModelAndView list(@RequestBody(required = false) Pageable pageable) {
        ModelAndView mv = new ModelAndView();
        if (libraryService.count() > 0) {
            mv.setViewName("view/word_group/list");
            mv.addObject("page", wordGroupService.findPage(pageable));
        } else {
            mv.setViewName("view/library/lack");
        }
        return mv;
    }

    /**
     * 添加单词分组
     * @return ModelAndView
     */
    @RequestMapping(value = "/add.html", method = {RequestMethod.GET, RequestMethod.POST})
    @WindowView
    protected ModelAndView add() {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
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
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
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
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
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
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
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
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
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
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
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
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
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
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
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
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
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

    /**
     * 单词分组详情
     * @param id 单词分组id
     * @return ModelAndView
     */
    @NavigationView
    @RequestMapping(value = "detail.html", method = {RequestMethod.GET, RequestMethod.POST})
    protected ModelAndView detail(Integer id) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        Assert.notNull(id, "word group id不能为null");
        Group wordGroup = wordGroupService.find(id);
        Assert.notNull(wordGroup, "word group不存在");
        ModelAndView mv = new ModelAndView("view/word_group/detail");
        mv.addObject("group", wordGroup);
        mv.addObject("students", ClazzStudentTreeVo.convert(wordGroup.getStudentsOriented()));
        return mv;
    }

    @WindowView
    @RequestMapping(value = "score.html", method = {RequestMethod.GET, RequestMethod.POST})
    protected ModelAndView score(Integer id) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        Assert.notNull(id, "word group id不能为null");
        Group wordGroup = wordGroupService.find(id);
        Assert.notNull(wordGroup, "word group不存在");

        /* 获取未评分的学生 */
        List<Student> studentsOriented = new ArrayList<>(wordGroup.getStudentsOriented());
        Assert.notEmpty(studentsOriented, "分组中没有学生");
        Set<Student> studentsScored = wordGroup.getStudentsScored();
        studentsOriented.removeAll(studentsScored);

        Assert.notEmpty(studentsOriented, "评分完毕");

        boolean hasMore = studentsOriented.size() > 1;

        Student student = studentsOriented.get(0);

        /* 分离已掌握的单词和未掌握的单词 */
        Set<Word> masteredWords = new LinkedHashSet<>(student.getMasteredWords());
        Set<Word> failedWords = new LinkedHashSet<>(wordGroup.getWords());
        masteredWords.retainAll(failedWords);
        failedWords.removeAll(masteredWords);

        ModelAndView mv = new ModelAndView("view/word_group/score");
        mv.addObject("group", wordGroup);
        mv.addObject("student", student);
        mv.addObject("hasMore", hasMore);
        mv.addObject("masteredWords", masteredWords);
        mv.addObject("failedWords", failedWords);
        return mv;
    }

    @PostMapping(value = "score_save.json")
    protected BaseVo scoreSave(@RequestParam Integer groupId, @RequestParam Integer studentId, @RequestParam(value = "wordId", required = false) Integer... masteredWordIds) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        Assert.notNull(groupId, "word group id不能为null");
        Group wordGroup = wordGroupService.find(groupId);
        Assert.notNull(wordGroup, "word group不存在");
        Assert.notNull(studentId, "student id不能为null");
        Student student = studentService.find(studentId);
        Assert.notNull(student, "student不存在");
        if (masteredWordIds == null) masteredWordIds = new Integer[0];
        Assert.noNullElements(masteredWordIds, "mastered word ids不能存在为null的元素");
        Word[] masteredWords = Arrays.stream(masteredWordIds).map(wordService::find).toArray(Word[]::new);
        wordGroupService.scoreWords(wordGroup, student, masteredWords);
        return SUCCESS_VO;
    }
}

package com.benzolamps.dict.controller;

import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.bean.StudyLog;
import com.benzolamps.dict.bean.StudyProcess;
import com.benzolamps.dict.controller.interceptor.NavigationView;
import com.benzolamps.dict.controller.interceptor.WindowView;
import com.benzolamps.dict.controller.vo.BaseVo;
import com.benzolamps.dict.controller.vo.DataVo;
import com.benzolamps.dict.controller.vo.StudentVo;
import com.benzolamps.dict.dao.core.Filter;
import com.benzolamps.dict.dao.core.Page;
import com.benzolamps.dict.dao.core.Pageable;
import com.benzolamps.dict.service.base.*;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;

import static com.benzolamps.dict.bean.Group.Status.NORMAL;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 学生Controller
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-20 18:57:56
 */
@RestController
@RequestMapping("student")
public class StudentController extends BaseController {

    @Resource
    private StudentService studentService;

    @Resource
    private ClazzService clazzService;

    @Resource
    private WordGroupService wordGroupService;

    @Resource
    private PhraseGroupService phraseGroupService;

    @Resource
    private StudyLogService studyLogService;

    @Resource
    private WordService wordService;

    @Resource
    private PhraseService phraseService;

    /**
     * 列出所有学生
     * @return ModelAndView
     */
    @RequestMapping(value = "list.html", method = {GET, POST})
    @NavigationView
    protected ModelAndView list(@RequestBody(required = false) Pageable pageable) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("view/student/list");
        Page<Student> students = studentService.findPage(pageable);
        mv.addObject("clazzes", clazzService.findAll());
        mv.addObject("page", students.convertPage(student -> {
            StudentVo studentVo = StudentVo.convertFromStudent(student);
            if (student.getDescription() != null)
            studentVo.setName(student.getName() + "（" + student.getDescription() + "）");
            studentVo.setClazz(student.getClazz().getName());
            return studentVo;
        }));
        return mv;
    }

    /**
     * 添加学生
     * @return ModelAndView
     */
    @RequestMapping(value = "add.html", method = {GET, POST})
    @WindowView
    protected ModelAndView add() {
        ModelAndView mv = new ModelAndView("view/student/add");
        mv.addObject("clazzes", clazzService.findAll());
        return mv;
    }

    /**
     * 保存学生
     * @param studentVo 学生
     * @return 保存后的学生
     */
    @PostMapping("save.json")
    @ResponseBody
    protected DataVo save(@RequestBody StudentVo studentVo) {
        Student student = StudentVo.convertToStudent(studentVo);
        studentService.persist(student);
        return wrapperData(student);
    }

    /**
     * 修改学生
     * @param id id
     * @return ModelAndView
     */
    @RequestMapping(value = "edit.html", method = {GET, POST})
    @WindowView
    protected ModelAndView edit(Integer id) {
        ModelAndView mv = new ModelAndView("view/student/edit");
        mv.addObject("student", StudentVo.convertFromStudent(studentService.find(id)));
        mv.addObject("clazzes", clazzService.findAll());
        return mv;
    }

    /**
     * 更新学生
     * @param studentVo 学生
     * @return 更新后的学生
     */
    @PostMapping("update.json")
    @ResponseBody
    protected DataVo update(@RequestBody StudentVo studentVo) {
        Student student = StudentVo.convertToStudent(studentVo);
        studentService.update(student);
        return wrapperData(student);
    }

    /**
     * 删除学生
     * @return 提示信息
     */
    @PostMapping("delete.json")
    @ResponseBody
    protected BaseVo delete(@RequestParam("id") Integer... ids) {
        studentService.remove(ids);
        return SUCCESS_VO;
    }

    /**
     * 检测学号是否已存在
     * @param number 学号
     * @return 检测结果
     */
    @RequestMapping(value = "number_not_exists.json", method = {GET, POST})
    @ResponseBody
    protected boolean numberNotExists(Integer number) {
        return !studentService.numberExists(number);
    }

    /**
     * 添加到单词分组
     * @param studentIds 学生id
     * @return ModelAndView
     */
    @WindowView
    @GetMapping("add_to_word_group.html")
    protected ModelAndView addToWordGroup(@RequestParam("studentId") Integer... studentIds) {
        ModelAndView mv = new ModelAndView("view/student/add_to_word_group");
        mv.addObject("groups", wordGroupService.findList(Filter.eq("status", NORMAL)));
        mv.addObject("students", studentService.findList(Filter.in("id", Arrays.asList(studentIds))));
        return mv;
    }

    /**
     * 添加到短语分组
     * @param studentIds 学生id
     * @return ModelAndView
     */
    @WindowView
    @GetMapping("add_to_phrase_group.html")
    protected ModelAndView addToPhraseGroup(@RequestParam("studentId") Integer... studentIds) {
        ModelAndView mv = new ModelAndView("view/student/add_to_phrase_group");
        mv.addObject("groups", phraseGroupService.findList(Filter.eq("status", NORMAL)));
        mv.addObject("students", studentService.findList(Filter.in("id", Arrays.asList(studentIds))));
        return mv;
    }

    /**
     * 学生详情
     * @param id 学生id
     * @return ModelAndView
     */
    @NavigationView
    @RequestMapping(value = "detail.html", method = {GET, POST})
    protected ModelAndView detail(Integer id) {
        Assert.notNull(id, "id不能为null");
        Student student = studentService.find(id);
        Assert.notNull(student, "student不存在");
        Collection<StudyLog> wordStudyLogs = studyLogService.findWordLogs(student);
        Collection<StudyLog> phraseStudyLogs = studyLogService.findPhraseLogs(student);
        ModelAndView mv = new ModelAndView("view/student/detail");
        mv.addObject("student", student);
        mv.addObject("wordStudyLogs", wordStudyLogs);
        mv.addObject("phraseStudyLogs", phraseStudyLogs);
        StudyProcess[] studyProcessVos = studentService.getStudyProcess(student);
        mv.addObject("wordStudyProcess", studyProcessVos[0]);
        mv.addObject("phraseStudyProcess", studyProcessVos[1]);
        mv.addObject("averageWordStudyProcess", studyProcessVos[2]);
        mv.addObject("averagePhraseStudyProcess", studyProcessVos[3]);
        return mv;
    }
}

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
import com.benzolamps.dict.controller.vo.ProcessImportVo;
import com.benzolamps.dict.dao.core.Pageable;
import com.benzolamps.dict.service.base.*;
import com.benzolamps.dict.util.Constant;
import com.benzolamps.dict.util.lambda.Func1;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.function.Consumer;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

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
    @RequestMapping(value = "list.html", method = {GET, POST})
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
    @RequestMapping(value = "add.html", method = {GET, POST})
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
    @PostMapping("save.json")
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
    @RequestMapping(value = "edit.html", method = {GET, POST})
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
    @PostMapping("update.json")
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
    @PostMapping("delete.json")
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
    @RequestMapping(value = "name_not_exists.json", method = {GET, POST})
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
    @PostMapping("add_words.json")
    @ResponseBody
    protected BaseVo addWords(@RequestParam("groupId") Integer[] wordGroupIds, @RequestParam("wordId") Integer... wordIds) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        Assert.notEmpty(wordGroupIds, "word group ids不能为null或空");
        Assert.noNullElements(wordGroupIds, "word group ids不能存在为null的元素");
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
    @PostMapping("add_students.json")
    @ResponseBody
    protected BaseVo addStudents(@RequestParam("groupId") Integer[] wordGroupIds, @RequestParam("studentId") Integer... studentIds) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        Assert.notEmpty(wordGroupIds, "word group ids不能为null或空");
        Assert.noNullElements(wordGroupIds, "word group ids不能存在为null的元素");
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
    @PostMapping("add_clazzes.json")
    @ResponseBody
    protected BaseVo addClazzes(@RequestParam("groupId") Integer[] wordGroupIds, @RequestParam("clazzId") Integer... clazzIds) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        Assert.notEmpty(wordGroupIds, "word group ids不能为null或空");
        Assert.noNullElements(wordGroupIds, "word group ids不能存在为null的元素");
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
     * 移除单词
     * @param wordGroupId 单词分组id
     * @param wordIds 单词id
     * @return 移除成功
     */
    @PostMapping("remove_words.json")
    @ResponseBody
    protected BaseVo removeWords(@RequestParam("groupId") Integer wordGroupId, @RequestParam("wordId") Integer[] wordIds) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        Assert.notNull(wordGroupId, "word group id不能为null或空");
        Group wordGroup = wordGroupService.find(wordGroupId);
        Assert.notNull(wordGroup, "word group不存在");
        Assert.noNullElements(wordIds, "clazz ids不能存在为null的元素");
        Word[] words = Arrays.stream(wordIds).map(wordService::find).toArray(Word[]::new);
        wordGroupService.removeWords(wordGroup, words);
        return SUCCESS_VO;
    }

    /**
     * 移除学生
     * @param wordGroupId 单词分组id
     * @param studentIds 学生id
     * @return 移除成功
     */
    @PostMapping("remove_students.json")
    @ResponseBody
    protected BaseVo removeStudents(@RequestParam("groupId") Integer wordGroupId, @RequestParam("studentId") Integer[] studentIds) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        Assert.notNull(wordGroupId, "word group id不能为null或空");
        Group wordGroup = wordGroupService.find(wordGroupId);
        Assert.notNull(wordGroup, "word group不存在");
        Assert.noNullElements(studentIds, "student ids不能存在为null的元素");
        Student[] students = Arrays.stream(studentIds).map(studentService::find).toArray(Student[]::new);
        wordGroupService.removeStudents(wordGroup, students);
        return SUCCESS_VO;
    }

    /**
     * 单词分组详情
     * @param id 单词分组id
     * @return ModelAndView
     */
    @NavigationView
    @RequestMapping(value = "detail.html", method = {GET, POST})
    protected ModelAndView detail(Integer id) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        Assert.notNull(id, "word group id不能为null");
        Group wordGroup = wordGroupService.find(id);
        Assert.notNull(wordGroup, "word group不存在");

        ModelAndView mv = new ModelAndView("view/word_group/detail");
        mv.addObject("group", wordGroup);
        if (wordGroup.getStatus() != Group.Status.COMPLETED) {
            mv.addObject("students", ClazzStudentTreeVo.convert(wordGroup.getStudentsOriented()));
        } else {
            mv.addObject("students", ClazzStudentTreeVo.convert(wordGroup.getGroupLog().getStudents()));
        }
        return mv;
    }

    /**
     * 评分界面
     * @param id 单词分组id
     * @return ModelAndView
     */
    @SuppressWarnings("ConstantConditions")
    @WindowView
    @RequestMapping(value = "score.html", method = {GET, POST})
    protected ModelAndView score(Integer id, Integer studentId) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        Assert.notNull(id, "word group id不能为null");
        Group wordGroup = wordGroupService.find(id);
        Assert.notNull(wordGroup, "word group不存在");

        /* 获取未评分的学生 */
        List<Student> studentsOriented = new ArrayList<>(wordGroup.getStudentsOriented());
        Assert.notEmpty(studentsOriented, "分组中没有学生");
        Set<Student> studentsScored = wordGroup.getStudentsScored();
        studentsOriented.removeAll(studentsScored);

        if (studentId == null) {
            Assert.notEmpty(studentsOriented, "评分完毕");
        }

        boolean hasMore = studentsOriented.size() > 1;

        ModelAndView mv = new ModelAndView("view/word_group/score");
        Student student;
        if (studentId != null) {
            student = wordGroup.getStudentsOriented().stream().filter(stu -> studentId.equals(stu.getId())).findFirst().get();
            mv.addObject("scored", !studentsOriented.contains(student));
        } else {
            student = studentsOriented.get(Constant.RANDOM.nextInt(studentsOriented.size()));
            mv.addObject("scored", false);
        }

        /* 分离已掌握的单词和未掌握的单词 */
        Collection<Word> masteredWords = new LinkedHashSet<>(student.getMasteredWords());
        Collection<Word> failedWords = new LinkedHashSet<>(wordGroup.getWords());
        masteredWords.retainAll(failedWords);
        failedWords.removeAll(masteredWords);
        masteredWords = Group.getFrequencySortedWords(masteredWords, wordGroup);
        failedWords = Group.getFrequencySortedWords(failedWords, wordGroup);

        mv.addObject("group", wordGroup);
        mv.addObject("student", student);
        mv.addObject("students", studentsOriented);
        mv.addObject("hasMore", hasMore);
        mv.addObject("masteredWords", masteredWords);
        mv.addObject("failedWords", failedWords);
        return mv;
    }

    /**
     * 评分保存
     * @param groupId 分组id
     * @param studentId 学生id
     * @param masteredWordIds 掌握的单词id
     * @return 保存成功
     */
    @PostMapping("score_save.json")
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

    /**
     * 跳过当前学生的评分
     * @param groupId 分组id
     * @param studentId 学生id
     * @return 操作成功
     */
    @PostMapping("jump.json")
    protected BaseVo jump(Integer groupId, Integer studentId) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        Assert.notNull(groupId, "id不能为null");
        Group wordGroup = wordGroupService.find(groupId);
        Assert.notNull(wordGroup, "word group不存在");
        Assert.notNull(studentId, "id不能为null");
        Student student = studentService.find(studentId);
        Assert.notNull(student, "student不存在");
        wordGroupService.jump(wordGroup, student);
        return SUCCESS_VO;
    }

    /**
     * 结束评分
     * @param id 分组id
     * @return 操作成功
     */
    @PostMapping("finish.json")
    protected BaseVo finish(Integer id) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        Assert.notNull(id, "id不能为null");
        Group wordGroup = wordGroupService.find(id);
        Assert.notNull(wordGroup, "word group不存在");
        wordGroupService.finish(wordGroup);
        return SUCCESS_VO;
    }

    /**
     * 完成
     * @param id 分组id
     * @return 操作成功
     */
    @PostMapping("complete.json")
    protected BaseVo complete(Integer id) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        Assert.notNull(id, "id不能为null");
        Group wordGroup = wordGroupService.find(id);
        Assert.notNull(wordGroup, "word group不存在");
        wordGroupService.complete(wordGroup);
        return SUCCESS_VO;
    }

    /**
     * 导入学习进度
     * @param groupId 分组id
     * @param studentId 学生id
     * @param files 文件
     * @return 操作成功
     */
    @PostMapping(value = "import.json")
    protected BaseVo imports(Integer groupId, Integer studentId, @RequestParam("file") MultipartFile... files) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        wordGroupService.importWords(
            Arrays.stream(files)
                .map((Func1<MultipartFile, ProcessImportVo>) file -> new ProcessImportVo(groupId, studentId, file.getOriginalFilename(), file.getBytes()))
                .toArray(ProcessImportVo[]::new)
        );
        return SUCCESS_VO;
    }

    /**
     * 添加词频分组界面
     * @return ModelAndView
     */
    @WindowView
    @RequestMapping(value = "add_frequency_group.html", method = {GET, POST})
    protected ModelAndView addFrequencyGroup() {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        return new ModelAndView("view/word_group/add_frequency_group");
    }

    /**
     * 保存词频分组
     * @param wordGroup 分组
     * @param file 文件
     * @return 操作成功
     */
    @ResponseBody
    @PostMapping(value = "add_frequency_group_save.json")
    protected BaseVo addFrequencyGroupSave(Group wordGroup, MultipartFile file) throws IOException {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        List<String> extraWords = new ArrayList<>();
        if (file.getOriginalFilename().endsWith(".doc") || file.getOriginalFilename().endsWith(".docx")) {
            wordGroup = wordGroupService.persistFrequencyGroupDoc(wordGroup, file.getBytes(), extraWords);
        } else {
            wordGroup = wordGroupService.persistFrequencyGroupTxt(wordGroup, file.getBytes(), extraWords);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("wordGroup", wordGroup);
        if (CollectionUtils.isEmpty(extraWords)) {
            data.put("hasExtraWords", false);
        } else {
            String token = UUID.randomUUID().toString().replace("-", "");
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            requestAttributes.setAttribute(token, (Consumer<OutputStream>) ops -> {

            }, RequestAttributes.SCOPE_SESSION);
            data.put("hasExtraWords", true);
            data.put("token", token);
        }

        return wrapperData(data);
    }
}

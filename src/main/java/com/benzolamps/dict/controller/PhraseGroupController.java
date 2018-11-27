package com.benzolamps.dict.controller;

import com.benzolamps.dict.bean.Clazz;
import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.bean.Phrase;
import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.controller.interceptor.NavigationView;
import com.benzolamps.dict.controller.interceptor.WindowView;
import com.benzolamps.dict.controller.vo.*;
import com.benzolamps.dict.dao.core.Pageable;
import com.benzolamps.dict.service.base.*;
import com.benzolamps.dict.util.Constant;
import com.benzolamps.dict.util.lambda.Action1;
import com.benzolamps.dict.util.lambda.Func1;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

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

    @Resource
    private LibraryService libraryService;

    /**
     * 列出所有短语分组
     * @return ModelAndView
     */
    @RequestMapping(value = "list.html", method = {GET, POST})
    @NavigationView
    protected ModelAndView list(@RequestBody(required = false) Pageable pageable) {
        ModelAndView mv = new ModelAndView();
        if (libraryService.count() > 0) {
            mv.setViewName("view/phrase_group/list");
            mv.addObject("page", phraseGroupService.findPage(pageable));
            mv.addObject("maxInfo", phraseGroupService.findMaxInfo());
        } else {
            mv.setViewName("view/library/lack");
        }
        return mv;
    }

    /**
     * 添加短语分组
     * @return ModelAndView
     */
    @RequestMapping(value = "add.html", method = {GET, POST})
    @WindowView
    protected ModelAndView add() {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        return new ModelAndView("view/phrase_group/add");
    }

    /**
     * 保存短语分组
     * @param phraseGroup 短语分组
     * @return 保存后的短语分组
     */
    @PostMapping("save.json")
    @ResponseBody
    protected DataVo save(@RequestBody Group phraseGroup) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        phraseGroup = phraseGroupService.persist(phraseGroup);
        return wrapperData(phraseGroup);
    }

    /**
     * 修改短语分组
     * @param id id
     * @return ModelAndView
     */
    @RequestMapping(value = "edit.html", method = {GET, POST})
    @WindowView
    protected ModelAndView edit(Integer id) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        ModelAndView mv = new ModelAndView("view/phrase_group/edit");
        mv.addObject("phraseGroup", phraseGroupService.find(id));
        return mv;
    }

    /**
     * 更新短语分组
     * @param phraseGroup 短语分组
     * @return 更新后的短语分组
     */
    @PostMapping("update.json")
    @ResponseBody
    protected DataVo update(@RequestBody Group phraseGroup) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        phraseGroup = phraseGroupService.update(phraseGroup, "status");
        return wrapperData(phraseGroup);
    }

    /**
     * 删除短语分组
     * @return 提示信息
     */
    @PostMapping("delete.json")
    @ResponseBody
    protected BaseVo delete(@RequestParam("id") Integer... ids) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        phraseGroupService.remove(ids);
        return SUCCESS_VO;
    }

    /**
     * 检测分组名称是否已存在
     * @param name 名称
     * @return 检测结果
     */
    @RequestMapping(value = "name_not_exists.json", method = {GET, POST})
    @ResponseBody
    protected boolean nameNotExists(String name) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        return !phraseGroupService.nameExists(name);
    }

    /**
     * 添加短语
     * @param phraseGroupIds 短语分组id
     * @param phraseIds 短语id
     * @return 添加成功
     */
    @PostMapping("add_phrases.json")
    @ResponseBody
    protected BaseVo addPhrases(@RequestParam("groupId") Integer[] phraseGroupIds, @RequestParam("phraseId") Integer... phraseIds) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        Assert.notEmpty(phraseGroupIds, "phrase group ids不能为null或空");
        Assert.noNullElements(phraseGroupIds, "phrase group ids不能存在为null的元素");
        for (Integer phraseGroupId : phraseGroupIds) {
            Group phraseGroup = phraseGroupService.find(phraseGroupId);
            Assert.notNull(phraseGroup, "phrase group不存在");
            Assert.noNullElements(phraseIds, "phrase ids不能存在为null的元素");
            Phrase[] phrases = Stream.of(phraseIds).map(phraseService::find).toArray(Phrase[]::new);
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
    @PostMapping("add_students.json")
    @ResponseBody
    protected BaseVo addStudents(@RequestParam("groupId") Integer[] phraseGroupIds, @RequestParam("studentId") Integer... studentIds) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        Assert.notEmpty(phraseGroupIds, "phrase group ids不能为null或空");
        Assert.noNullElements(phraseGroupIds, "phrase group ids不能存在为null的元素");
        for (Integer phraseGroupId : phraseGroupIds) {
            Group phraseGroup = phraseGroupService.find(phraseGroupId);
            Assert.notNull(phraseGroup, "phrase group不存在");
            Assert.noNullElements(studentIds, "student ids不能存在为null的元素");
            Student[] students = Stream.of(studentIds).map(studentService::find).toArray(Student[]::new);
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
    @PostMapping("add_clazzes.json")
    @ResponseBody
    protected BaseVo addClazzes(@RequestParam("groupId") Integer[] phraseGroupIds, @RequestParam("clazzId") Integer... clazzIds) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        Assert.notEmpty(phraseGroupIds, "phrase group ids不能为null或空");
        Assert.noNullElements(phraseGroupIds, "phrase group ids不能存在为null的元素");
        for (Integer phraseGroupId : phraseGroupIds) {
            Group phraseGroup = phraseGroupService.find(phraseGroupId);
            Assert.notNull(phraseGroup, "phrase group不存在");
            Assert.noNullElements(clazzIds, "clazz ids不能存在为null的元素");
            Clazz[] clazzes = Stream.of(clazzIds).map(clazzService::find).toArray(Clazz[]::new);
            phraseGroupService.addClazzes(phraseGroup, clazzes);
        }
        return SUCCESS_VO;
    }

    /**
     * 移除短语
     * @param phraseGroupId 短语分组id
     * @param phraseIds 短语id
     * @return 移除成功
     */
    @PostMapping("remove_phrases.json")
    @ResponseBody
    protected BaseVo removePhrases(@RequestParam("groupId") Integer phraseGroupId, @RequestParam("phraseId") Integer[] phraseIds) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        Assert.notNull(phraseGroupId, "phrase group id不能为null或空");
        Group phraseGroup = phraseGroupService.find(phraseGroupId);
        Assert.notNull(phraseGroup, "phrase group不存在");
        Assert.noNullElements(phraseIds, "clazz ids不能存在为null的元素");
        Phrase[] phrases = Stream.of(phraseIds).map(phraseService::find).toArray(Phrase[]::new);
        phraseGroupService.removePhrases(phraseGroup, phrases);
        return SUCCESS_VO;
    }

    /**
     * 移除学生
     * @param phraseGroupId 短语分组id
     * @param studentIds 学生id
     * @return 移除成功
     */
    @PostMapping("remove_students.json")
    @ResponseBody
    protected BaseVo removeStudents(@RequestParam("groupId") Integer phraseGroupId, @RequestParam("studentId") Integer[] studentIds) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        Assert.notNull(phraseGroupId, "phrase group id不能为null或空");
        Group phraseGroup = phraseGroupService.find(phraseGroupId);
        Assert.notNull(phraseGroup, "phrase group不存在");
        Assert.noNullElements(studentIds, "student ids不能存在为null的元素");
        Student[] students = Stream.of(studentIds).map(studentService::find).toArray(Student[]::new);
        phraseGroupService.removeStudents(phraseGroup, students);
        return SUCCESS_VO;
    }

    /**
     * 短语分组详情
     * @param id 短语分组id
     * @return ModelAndView
     */
    @NavigationView
    @RequestMapping(value = "detail.html", method = {GET, POST})
    protected ModelAndView detail(Integer id) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        Assert.notNull(id, "phrase group id不能为null");
        Group phraseGroup = phraseGroupService.find(id);
        Assert.notNull(phraseGroup, "phrase group不存在");

        ModelAndView mv = new ModelAndView("view/phrase_group/detail");
        mv.addObject("group", phraseGroup);
        if (phraseGroup.getStatus() != Group.Status.COMPLETED) {
            mv.addObject("students", ClazzStudentTreeVo.convert(phraseGroup.getStudentsOriented()));
        } else {
            mv.addObject("students", ClazzStudentTreeVo.convert(phraseGroup.getGroupLog().getStudents()));
        }
        return mv;
    }

    /**
     * 评分界面
     * @param id 短语分组id
     * @return ModelAndView
     */
    @SuppressWarnings("ConstantConditions")
    @WindowView
    @RequestMapping(value = "score.html", method = {GET, POST})
    protected ModelAndView score(Integer id, Integer studentId) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        Assert.notNull(id, "phrase group id不能为null");
        Group phraseGroup = phraseGroupService.find(id);
        Assert.notNull(phraseGroup, "phrase group不存在");

        /* 获取未评分的学生 */
        List<Student> studentsOriented = new ArrayList<>(phraseGroup.getStudentsOriented());
        Assert.notEmpty(studentsOriented, "分组中没有学生");
        Set<Student> studentsScored = phraseGroup.getStudentsScored();
        studentsOriented.removeAll(studentsScored);

        if (studentId == null) {
            Assert.notEmpty(studentsOriented, "评分完毕");
        }

        boolean hasMore = studentsOriented.size() > 1;

        ModelAndView mv = new ModelAndView("view/phrase_group/score");
        Student student;
        if (studentId != null) {
            student = phraseGroup.getStudentsOriented().stream().filter(stu -> studentId.equals(stu.getId())).findFirst().get();
            mv.addObject("scored", !studentsOriented.contains(student));
        } else {
            student = studentsOriented.get(Constant.RANDOM.nextInt(studentsOriented.size()));
            mv.addObject("scored", false);
        }

        /* 分离已掌握的短语和未掌握的短语 */
        Collection<Phrase> masteredPhrases = new LinkedHashSet<>(student.getMasteredPhrases());
        Collection<Phrase> failedPhrases = new LinkedHashSet<>(phraseGroup.getPhrases());
        masteredPhrases.retainAll(failedPhrases);
        failedPhrases.removeAll(masteredPhrases);
        masteredPhrases = Group.getFrequencySortedPhrases(masteredPhrases, phraseGroup);
        failedPhrases = Group.getFrequencySortedPhrases(failedPhrases, phraseGroup);

        mv.addObject("group", phraseGroup);
        mv.addObject("student", student);
        mv.addObject("students", studentsOriented);
        mv.addObject("hasMore", hasMore);
        mv.addObject("masteredPhrases", masteredPhrases);
        mv.addObject("failedPhrases", failedPhrases);
        return mv;
    }

    /**
     * 评分保存
     * @param groupId 短语分组id
     * @param studentId 学生id
     * @param masteredPhraseIds 掌握的短语id
     * @return 保存成功
     */
    @PostMapping("score_save.json")
    protected BaseVo scoreSave(Integer groupId, @RequestParam Integer studentId, @RequestParam(value = "phraseId", required = false) Integer... masteredPhraseIds) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        Assert.notNull(groupId, "phrase group id不能为null");
        Group phraseGroup = phraseGroupService.find(groupId);
        Assert.notNull(phraseGroup, "phrase group不存在");
        Assert.notNull(studentId, "student id不能为null");
        Student student = studentService.find(studentId);
        Assert.notNull(student, "student不存在");
        if (masteredPhraseIds == null) masteredPhraseIds = new Integer[0];
        Assert.noNullElements(masteredPhraseIds, "mastered phrase ids不能存在为null的元素");
        Phrase[] masteredPhrases = Stream.of(masteredPhraseIds).map(phraseService::find).toArray(Phrase[]::new);
        phraseGroupService.scorePhrases(phraseGroup, student, masteredPhrases);
        return SUCCESS_VO;
    }

    /**
     * 跳过当前学生的评分
     * @param groupId 短语分组id
     * @param studentId 学生id
     * @return 操作成功
     */
    @PostMapping("jump.json")
    protected BaseVo jump(Integer groupId, Integer studentId) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        Assert.notNull(groupId, "id不能为null");
        Group phraseGroup = phraseGroupService.find(groupId);
        Assert.notNull(phraseGroup, "phrase group不存在");
        Assert.notNull(studentId, "id不能为null");
        Student student = studentService.find(studentId);
        Assert.notNull(student, "student不存在");
        phraseGroupService.jump(phraseGroup, student);
        return SUCCESS_VO;
    }

    /**
     * 结束评分
     * @param id 短语分组id
     * @return 操作成功
     */
    @PostMapping("finish.json")
    protected BaseVo finish(Integer id) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        Assert.notNull(id, "id不能为null");
        Group phraseGroup = phraseGroupService.find(id);
        Assert.notNull(phraseGroup, "phrase group不存在");
        phraseGroupService.finish(phraseGroup);
        return SUCCESS_VO;
    }

    /**
     * 完成评分
     * @param id 短语分组id
     * @return 操作成功
     */
    @PostMapping("complete.json")
    protected BaseVo complete(Integer id) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        Assert.notNull(id, "id不能为null");
        Group phraseGroup = phraseGroupService.find(id);
        Assert.notNull(phraseGroup, "phrase group不存在");
        phraseGroupService.complete(phraseGroup);
        return SUCCESS_VO;
    }

    /**
     * 导入短语学习进度
     * @param groupId 短语分组id
     * @param studentId 学生id
     * @param files 文件
     * @return 操作成功
     */
    @PostMapping(value = "import.json")
    protected BaseVo imports(Integer groupId, Integer studentId, @RequestParam("file") MultipartFile... files) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        phraseGroupService.importPhrases(
                Stream.of(files)
                        .map((Func1<MultipartFile, ProcessImportVo>) file -> new ProcessImportVo(groupId, studentId, file.getOriginalFilename(), file.getBytes()))
                        .toArray(ProcessImportVo[]::new)
        );
        return SUCCESS_VO;
    }

    /**
     * 添加短语词频分组界面
     * @return ModelAndView
     */
    @WindowView
    @RequestMapping(value = "add_frequency_group.html", method = {GET, POST})
    protected ModelAndView addFrequencyGroup() {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        return new ModelAndView("view/phrase_group/add_frequency_group");
    }

    /**
     * 保存短语词频分组
     * @param phraseGroup 短语分组
     * @param file 文件
     * @param content 内容
     * @return 操作成功
     */
    @ResponseBody
    @PostMapping(value = "add_frequency_group_save.json")
    protected BaseVo addFrequencyGroupSave(Group phraseGroup, MultipartFile file, String content) throws IOException {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        Assert.isTrue(null != file || StringUtils.hasText(content), "参数错误");
        List<String> extraPhrases = new ArrayList<>();
        if (null != file) {
            if (file.getOriginalFilename().endsWith(".doc") || file.getOriginalFilename().endsWith(".docx")) {
                phraseGroup = phraseGroupService.persistFrequencyGroupDoc(phraseGroup, file.getBytes(), extraPhrases);
            } else {
                phraseGroup = phraseGroupService.persistFrequencyGroupTxt(phraseGroup, file.getBytes(), extraPhrases);
            }
        }
        if (StringUtils.hasText(content)) {
            phraseGroup = phraseGroupService.persistFrequencyGroupStr(phraseGroup, content, extraPhrases);
        }
        return makeupVo(phraseGroup, extraPhrases);
    }

    /**
     * 修改短语词频分组界面
     * @param id id
     * @return ModelAndView
     */
    @WindowView
    @RequestMapping(value = "edit_frequency_group.html", method = {GET, POST})
    protected ModelAndView editFrequencyGroup(Integer id) {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        Assert.notNull(id, "id不能为null");
        ModelAndView mv = new ModelAndView("view/phrase_group/edit_frequency_group");
        Group phraseGroup = phraseGroupService.find(id);
        Assert.notNull(phraseGroup, "phrase group不存在");
        Assert.isTrue(phraseGroup.getFrequencyGenerated(), "phrase group不是词频分组");
        mv.addObject("phraseGroup", phraseGroup);
        return mv;
    }

    /**
     * 更新短语词频分组
     * @param phraseGroup 短语分组
     * @param file 文件
     * @param content 内容
     * @return 操作成功
     */
    @ResponseBody
    @PostMapping(value = "edit_frequency_group_save.json")
    protected BaseVo updateFrequencyGroupSave(Group phraseGroup, MultipartFile file, String content) throws IOException {
        Assert.isTrue(libraryService.count() > 0, "未选择词库");
        Assert.isTrue(null != file || StringUtils.hasText(content), "参数错误");
        List<String> extraPhrases = new ArrayList<>();
        if (null != file) {
            if (file.getOriginalFilename().endsWith(".doc") || file.getOriginalFilename().endsWith(".docx")) {
                phraseGroup = phraseGroupService.updateFrequencyGroupDoc(phraseGroup, file.getBytes(), extraPhrases);
            } else {
                phraseGroup = phraseGroupService.updateFrequencyGroupTxt(phraseGroup, file.getBytes(), extraPhrases);
            }
        }
        if (StringUtils.hasText(content)) {
            phraseGroup = phraseGroupService.updateFrequencyGroupStr(phraseGroup, content, extraPhrases);
        }
        return makeupVo(phraseGroup, extraPhrases);
    }

    private BaseVo makeupVo(Group phraseGroup, List<String> extraPhrases) {
        Map<String, Object> data = new HashMap<>();
        data.put("phraseGroup", phraseGroup);
        if (CollectionUtils.isEmpty(extraPhrases)) {
            data.put("hasExtraPhrases", false);
        } else {
            String token = UUID.randomUUID().toString().replace("-", "");
            Map<String, Object> exportAttribute = new HashMap<>();
            exportAttribute.put("ext", "xlsx");
            exportAttribute.put("consumer", (Action1<OutputStream>) outputStream -> {
                List<Phrase> phrases = extraPhrases.stream().map(prototype -> {
                    Phrase phrase = new Phrase();
                    phrase.setPrototype(prototype);
                    return phrase;
                }).collect(Collectors.toList());
                phraseService.toExcel(phrases, outputStream);
            });
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            requestAttributes.setAttribute(token, exportAttribute, RequestAttributes.SCOPE_SESSION);
            data.put("hasExtraPhrases", true);
            data.put("token", token);
        }
        return wrapperData(data);
    }

    /**
     * 创建派生短语分组界面
     * @param groupId 短语分组id
     * @param phraseIds 短语id集合
     * @param studentIds 学生id集合
     * @return ModelAndView
     */
    @WindowView
    @RequestMapping(value = "extract_derive_group.html", method = {GET, POST})
    protected ModelAndView extractDeriveGroup(Integer groupId, @RequestParam(value = "phraseId", required = false) Integer[] phraseIds, @RequestParam(value = "studentId", required = false) Integer[] studentIds) {
        ModelAndView mv = new ModelAndView("view/phrase_group/extract_derive_group");
        Assert.isTrue(!(ObjectUtils.isEmpty(phraseIds) && ObjectUtils.isEmpty(studentIds)), "phrase ids和student ids不能同时为null或空");
        ExtractGroupVo<Phrase> extractGroupVo = new ExtractGroupVo<>(groupId, phraseIds, studentIds, phraseGroupService, phraseService, studentService);
        mv.addObject("phraseGroup", extractGroupVo.getGroup());
        mv.addObject("phrases", extractGroupVo.getPhrases());
        mv.addObject("students", extractGroupVo.getStudents());
        return mv;
    }

    /**
     * 创建派生短语分组
     * @param groupId 短语分组id
     * @param phraseIds 短语id集合
     * @param studentIds 学生id集合
     * @param phraseGroup 分组
     * @return 操作成功
     */
    @ResponseBody
    @PostMapping(value = "extract_derive_group_save.json")
    protected BaseVo extractDeriveGroupSave(Integer groupId, @RequestParam(value = "phraseId", required = false) Integer[] phraseIds, @RequestParam(value = "studentId", required = false) Integer[] studentIds, Group phraseGroup) {
        Assert.isTrue(!(ObjectUtils.isEmpty(phraseIds) && ObjectUtils.isEmpty(studentIds)), "phrase ids和student ids不能同时为null或空");
        ExtractGroupVo<Phrase> extractGroupVo = new ExtractGroupVo<>(groupId, phraseIds, studentIds, phraseGroupService, phraseService, studentService);
        return wrapperData(phraseGroupService.extractDeriveGroup(extractGroupVo.getGroup(), extractGroupVo.getPhrases(), extractGroupVo.getStudents(), phraseGroup));
    }

    /**
     * 创建专属短语分组界面
     * @param groupId 短语分组id
     * @param studentIds 学生id集合
     * @return ModelAndView
     */
    @WindowView
    @RequestMapping(value = "extract_personal_group.html", method = {GET, POST})
    protected ModelAndView extractPersonalGroup(Integer groupId, @RequestParam(value = "studentId", required = false) Integer[] studentIds) {
        Assert.notEmpty(studentIds, "student ids不能为null或空");
        ModelAndView mv = new ModelAndView("view/phrase_group/extract_personal_group");
        ExtractGroupVo<Phrase> extractGroupVo = new ExtractGroupVo<>(groupId, null, studentIds, phraseGroupService, phraseService, studentService);
        mv.addObject("phraseGroup", extractGroupVo.getGroup());
        mv.addObject("students", extractGroupVo.getStudents());
        return mv;
    }

    /**
     * 创建专属短语分组
     * @param groupId 短语分组id
     * @param studentIds 学生id集合
     * @param phraseGroup 短语分组
     * @return 操作成功
     */
    @ResponseBody
    @PostMapping(value = "extract_personal_group_save.json")
    protected BaseVo extractPersonalGroupSave(Integer groupId, @RequestParam(value = "studentId", required = false) Integer[] studentIds, Group phraseGroup) {
        Assert.notEmpty(studentIds, "student ids不能为null或空");
        ExtractGroupVo<Phrase> extractGroupVo = new ExtractGroupVo<>(groupId, null, studentIds, phraseGroupService, phraseService, studentService);
        return wrapperData(phraseGroupService.extractPersonalGroup(extractGroupVo.getGroup(), extractGroupVo.getStudents(), phraseGroup));
    }
}

package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.*;
import com.benzolamps.dict.bean.Group.Type;
import com.benzolamps.dict.cfg.AipProperties;
import com.benzolamps.dict.controller.vo.ProcessImportVo;
import com.benzolamps.dict.dao.base.GroupDao;
import com.benzolamps.dict.dao.core.Filter;
import com.benzolamps.dict.dao.core.Order;
import com.benzolamps.dict.exception.ProcessImportException;
import com.benzolamps.dict.service.base.*;
import com.benzolamps.dict.util.DictArray;
import com.benzolamps.dict.util.DictQrCode;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.benzolamps.dict.bean.Group.Status.*;
import static com.benzolamps.dict.bean.Group.Type.PHRASE;
import static com.benzolamps.dict.bean.Group.Type.WORD;

/**
 * 单词短语分组Service接口实现类
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-21 22:09:55
 */
@Slf4j
public abstract class GroupServiceImpl extends BaseServiceImpl<Group> implements GroupService {

    private final Type type;

    @Resource
    private GroupDao groupDao;

    @Resource
    private StudyLogService studyLogService;

    @Resource
    private LibraryService libraryService;

    @Resource
    private StudentService studentService;

    @Resource
    private AipProperties aipProperties;

    @Resource
    private WordService wordService;

    @Resource
    private PhraseService phraseService;

    protected GroupServiceImpl(Type type) {
        Assert.notNull(type, "type不能为null");
        this.type = type;
    }

    @Override
    public Group find(Integer id) {
        Library library = libraryService.getCurrent();
        Assert.notNull(library, "未选中词库");
        Group group = super.find(id);
        return library.equals(group.getLibrary()) && type.equals(group.getType()) ? group : null;
    }

    @Override
    public void persist(Group... groups) {
        Library library = libraryService.getCurrent();
        Assert.notNull(library, "未选中词库");
        Arrays.stream(groups).forEach(group -> {
            group.setType(type);
            group.setStatus(NORMAL);
            group.setLibrary(library);
            group.setScoreCount(0);
        });
        super.persist(groups);
    }

    @Override
    public Group persist(Group group) {
        Library library = libraryService.getCurrent();
        Assert.notNull(library, "未选中词库");
        group.setType(type);
        group.setStatus(NORMAL);
        group.setLibrary(library);
        group.setScoreCount(0);
        return super.persist(group);
    }

    @Override
    public void persist(Collection<Group> groups) {
        Library library = libraryService.getCurrent();
        Assert.notNull(library, "未选中词库");
        groups.forEach(group -> {
            group.setType(type);
            group.setStatus(NORMAL);
            group.setLibrary(library);
            group.setScoreCount(0);
        });
        super.persist(groups);
    }

    @Override
    public Group update(Group group, String... ignoreProperties) {
        Group ref = find(group.getId());
        if (!DictArray.contains(ignoreProperties, "name")) ref.setName(group.getName());
        if (!DictArray.contains(ignoreProperties, "description")) ref.setDescription(group.getDescription());
        return ref;
    }

    @Override
    public void update(Collection<Group> groups, String... ignoreProperties) {
        for (Group group : groups) {
            Group ref = find(group.getId());
            if (!DictArray.contains(ignoreProperties, "name")) ref.setName(group.getName());
            if (!DictArray.contains(ignoreProperties, "description")) ref.setDescription(group.getDescription());
        }
    }

    @Override
    public void remove(Collection<Group> groups) {
        Assert.isTrue(groups.stream().allMatch(Objects::nonNull), "groups中不能存在为null的元素");
        Assert.isTrue(groups.stream().allMatch(group -> group.getStatus() != SCORING), "无法删除正在评分的分组！");
        super.remove(groups);
    }

    @Override
    protected void handleFilter(final Filter filter) {
        Library library = libraryService.getCurrent();
        Assert.notNull(library, "未选中词库");
        filter.and(Filter.eq("type", type)).and(Filter.eq("library", library));
    }

    @Override
    protected void handleOrder(final List<Order> orders) {
        orders.add(Order.asc("createDate"));
    }

    @Transactional(readOnly = true)
    @Override
    public boolean nameExists(String name) {
        return groupDao.count(Filter.eq("name", name)) > 0;
    }

    @Transactional
    @Override
    public void addStudents(Group group, Student... students) {
        Assert.notNull(group, "group不能为null");
        Assert.isTrue(group.getType() == type, "group类型错误");
        group.getStudentsOriented().addAll(Arrays.asList(students));
    }

    @Transactional
    @Override
    public void addClazzes(Group group, Clazz... clazzes) {
        Assert.notNull(group, "group不能为null");
        Assert.noNullElements(clazzes, "clazzes不能存在为null的元素");
        Assert.isTrue(group.getType() == type, "group类型错误");
        Set<Student> students = new HashSet<>();
        for (Clazz clazz : clazzes) {
            students.addAll(clazz.getStudents());
        }
        this.addStudents(group, students.toArray(new Student[0]));
    }

    @Transactional
    @Override
    public void removeStudents(Group group, Student... students) {
        Assert.notNull(group, "group不能为null");
        Assert.isTrue(group.getType() == type, "group类型错误");
        List<Student> studentList = Arrays.asList(students);
        group.getStudentsOriented().removeAll(studentList);
        group.getStudentsScored().removeAll(studentList);
        if (group.getGroupLog() != null) {
            group.getGroupLog().getStudents().removeAll(studentList);
        }
    }

    @Transactional
    public void addWords(Group wordGroup, Word... words) {
        Assert.notNull(wordGroup, "word group不能为null");
        Assert.noNullElements(words, "words不能存在为null的元素");
        Assert.isTrue(type == WORD && wordGroup.getType() == type, "group类型错误");
        wordGroup.getWords().addAll(Arrays.asList(words));
    }

    @Transactional
    public void addPhrases(Group phraseGroup, Phrase... phrases) {
        Assert.notNull(phraseGroup, "phrase group不能为null");
        Assert.noNullElements(phrases, "phrases不能存在为null的元素");
        Assert.isTrue(type == PHRASE && phraseGroup.getType() == type, "group类型错误");
        phraseGroup.getPhrases().addAll(Arrays.asList(phrases));
    }

    @Transactional
    public void removeWords(Group wordGroup, Word... words) {
        Assert.notNull(wordGroup, "word group不能为null");
        Assert.noNullElements(words, "words不能存在为null的元素");
        Assert.isTrue(type == WORD && wordGroup.getType() == type, "group类型错误");
        List<Word> wordList = Arrays.asList(words);
        wordGroup.getWords().removeAll(wordList);
        if (wordGroup.getGroupLog() != null) {
            wordGroup.getGroupLog().getWords().removeAll(wordList);
        }

    }

    @Transactional
    public void removePhrases(Group phraseGroup, Phrase... phrases) {
        Assert.notNull(phraseGroup, "phrase group不能为null");
        Assert.noNullElements(phrases, "phrases不能存在为null的元素");
        Assert.isTrue(type == PHRASE && phraseGroup.getType() == type, "group类型错误");
        phraseGroup.getPhrases().removeAll(Arrays.asList(phrases));
        List<Phrase> phraseList = Arrays.asList(phrases);
        phraseGroup.getPhrases().removeAll(phraseList);
        if (phraseGroup.getGroupLog() != null) {
            phraseGroup.getGroupLog().getPhrases().removeAll(phraseList);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Transactional
    public void scoreWords(Group wordGroup, Student student, Word... words) {
        Assert.notNull(wordGroup, "word group不能为null");
        Assert.notNull(student, "student不能为null");
        Assert.notNull(words, "words不能为null");
        Assert.noNullElements(words, "words不能存在为null的元素");
        Assert.isTrue(type == WORD && wordGroup.getType() == type, "group类型错误");
        studentService.addFailedWords(student, wordGroup.getWords().toArray(new Word[0]));
        studentService.addMasteredWords(student, words);
        this.internalJump(wordGroup, student, words.length, null);
        for (Word word : words) {
            Word wordReview = wordGroup.getGroupLog().getWords().stream().filter(word::equals).findFirst().orElse(null);
            wordReview.setMasteredStudentsCount(wordReview.getMasteredStudentsCount() + 1);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Transactional
    public void scorePhrases(Group phraseGroup, Student student, Phrase... phrases) {
        Assert.notNull(phraseGroup, "phrase group不能为null");
        Assert.notNull(student, "student不能为null");
        Assert.notNull(phrases, "phrases不能为null");
        Assert.noNullElements(phrases, "phrases不能存在为null的元素");
        Assert.isTrue(type == PHRASE && phraseGroup.getType() == type, "group类型错误");
        studentService.addFailedPhrases(student, phraseGroup.getPhrases().toArray(new Phrase[0]));
        studentService.addMasteredPhrases(student, phrases);
        this.internalJump(phraseGroup, student, null, phrases.length);
        for (Phrase phrase : phrases) {
            Phrase phraseReview = phraseGroup.getGroupLog().getPhrases().stream().filter(phrase::equals).findFirst().orElse(null);
            phraseReview.setMasteredStudentsCount(phraseReview.getMasteredStudentsCount() + 1);
        }
    }

    @Transactional
    public void importWords(ProcessImportVo... processImportVos) {
        Assert.notEmpty(processImportVos, "process import vos不能为null或空");
        for (ProcessImportVo processImportVo : processImportVos) {
            if (processImportVo.getStudentId() == null) {
                try {
                    String content = DictQrCode.readQrCode(processImportVo.getData());
                    byte[] bytes = Base64.getDecoder().decode(content);
                    Random random = new Random(2018);
                    for (int i = 0; i < bytes.length; i++) {
                        bytes[i] ^= (random.nextInt(Byte.MAX_VALUE * 2) - Byte.MAX_VALUE);
                    }
                    String[] value = new String(bytes, "UTF-8").split(",");
                    Integer studentId = Integer.valueOf(value[0]);
                    Integer groupId = Integer.valueOf(value[1]);
                    processImportVo.setStudentId(studentId);
                    if (processImportVo.getGroupId() == null) {
                        processImportVo.setGroupId(groupId);
                    }
                } catch (Throwable e) {
                    throw new ProcessImportException(processImportVo.getName(), e);
                }
            }
        }
        internalImportWords(processImportVos);
    }

    private Collection<Word> getWords(byte[] data, String name, Collection<Word> ref) {
        List<String> words = new ArrayList<>();
        JSONObject res = aipProperties.accurateGeneral(data);
        for (int i = 0; i < res.getJSONArray("words_result").length(); i++) {
            String word = (String) res.getJSONArray("words_result").getJSONObject(i).get("words");
            if (word.matches("[ \\s\\u00a0]*[0-9]+[ \\s\\u00a0]*[.．][ \\s\\u00a0]*[^ \\s\\u00a0]+.*")) {
                word = word.substring(1 + word.split("[.．]")[0].length())
                    .replaceAll("[ \\s\\u00a0]+", " ")
                    .replaceAll("(^[ \\s\\u00a0]+)|([ \\s\\u00a0]+$)", "");
                words.add(word);
            }
        }
        Collection<Word> resultWords;
        if (!CollectionUtils.isEmpty(ref)) {
            resultWords = new ArrayList<>(ref);
            resultWords.removeIf(word -> !words.contains(word.getPrototype()));
        } else {
            resultWords = wordService.findByPrototypes(words);
        }
        logger.info(name + "导入的单词：" + String.join(", ", resultWords.stream().map(Word::getPrototype).collect(Collectors.toList())));
        return resultWords;
    }

    private void internalImportWords(ProcessImportVo... processImportVos) {
        Student student = new Student();
        Group group = new Group();
        Set<Word> words = new HashSet<>();
        Arrays.sort(processImportVos, (processImportVo1, processImportVo2) -> {
            int result = 0;
            if (processImportVo1.getGroupId() != null && processImportVo2.getGroupId() != null) {
                result = processImportVo1.getGroupId().compareTo(processImportVo2.getGroupId());
            }
            if (processImportVo1.getGroupId() == null && processImportVo2.getGroupId() != null) {
                result = -1;
            }
            if (processImportVo1.getGroupId() != null && processImportVo2.getGroupId() == null) {
                result = 1;
            }
            if (result == 0) {
                result = processImportVo1.getStudentId().compareTo(processImportVo2.getStudentId());
            }
            return result;
        });

        for (ProcessImportVo processImportVo : processImportVos) {
            try {
                Integer groupId = processImportVo.getGroupId();
                Integer studentId = processImportVo.getStudentId();
                if (groupId == null) {
                    if (null == group.getId()) {
                        if (studentId.equals(student.getId())) {
                            words.addAll(getWords(processImportVo.getData(), processImportVo.getName(), group.getWords()));
                        } else {
                            if (null != student.getId()) {
                                studentService.addMasteredWords(student, words.toArray(new Word[0]));
                            }
                            student = studentService.find(studentId);
                            words = new HashSet<>(getWords(processImportVo.getData(), processImportVo.getName(), group.getWords()));
                        }
                    } else {
                        if (studentId.equals(student.getId())) {
                            words.addAll(getWords(processImportVo.getData(), processImportVo.getName(), group.getWords()));
                        } else {
                            if (null != student.getId()) {
                                this.scoreWords(group, student, words.toArray(new Word[0]));
                            }
                            student = studentService.find(studentId);
                            words = new HashSet<>(getWords(processImportVo.getData(), processImportVo.getName(), group.getWords()));
                        }
                    }
                } else {
                    if (Objects.equals(group.getId(), groupId)) {
                        if (studentId.equals(student.getId())) {
                            words.addAll(getWords(processImportVo.getData(), processImportVo.getName(), group.getWords()));
                        } else {
                            if (null != student.getId()) {
                                this.scoreWords(group, student, words.toArray(new Word[0]));
                            }
                            student = studentService.find(studentId);
                            words = new HashSet<>(getWords(processImportVo.getData(), processImportVo.getName(), group.getWords()));
                        }
                    } else {
                        if (null != student.getId()) {
                            if (null != group.getId()) {
                                this.scoreWords(group, student, words.toArray(new Word[0]));
                            } else {
                                studentService.addMasteredWords(student, words.toArray(new Word[0]));
                            }
                        }
                        if (!studentId.equals(student.getId())) {
                            student = studentService.find(studentId);
                            Assert.notNull(student, "student不存在");
                        }
                        group = this.find(groupId);
                        Assert.notNull(group, "word group不存在");
                        words = new HashSet<>(getWords(processImportVo.getData(), processImportVo.getName(), group.getWords()));
                    }
                }
            } catch (Throwable e) {
                throw new ProcessImportException(processImportVo.getName(), e);
            }
        }
        if (null != student.getId()) {
            if (null != group.getId()) {
                this.scoreWords(group, student, words.toArray(new Word[0]));
            } else {
                studentService.addMasteredWords(student, words.toArray(new Word[0]));
            }
        }
    }

    @Transactional
    public void importPhrases(ProcessImportVo... processImportVos) {
        Assert.notEmpty(processImportVos, "process import vos不能为null或空");
        for (ProcessImportVo processImportVo : processImportVos) {
            if (processImportVo.getStudentId() == null) {
                try {
                    String content = DictQrCode.readQrCode(processImportVo.getData());
                    byte[] bytes = Base64.getDecoder().decode(content);
                    Random random = new Random(2018);
                    for (int i = 0; i < bytes.length; i++) {
                        bytes[i] ^= (random.nextInt(Byte.MAX_VALUE * 2) - Byte.MAX_VALUE);
                    }
                    String[] value = new String(bytes, "UTF-8").split(",");
                    Integer studentId = Integer.valueOf(value[0]);
                    Integer groupId = Integer.valueOf(value[1]);
                    processImportVo.setStudentId(studentId);
                    if (processImportVo.getGroupId() == null) {
                        processImportVo.setGroupId(groupId);
                    }
                } catch (Throwable e) {
                    throw new ProcessImportException(processImportVo.getName(), e);
                }
            }
        }
        internalImportPhrases(processImportVos);
    }

    private Collection<Phrase> getPhrases(byte[] data, String name, Collection<Phrase> ref) {
        List<String> phrases = new ArrayList<>();
        JSONObject res = aipProperties.accurateGeneral(data);
        for (int i = 0; i < res.getJSONArray("phrases_result").length(); i++) {
            String phrase = (String) res.getJSONArray("phrases_result").getJSONObject(i).get("phrases");
            if (phrase.matches("[ \\s\\u00a0]*[0-9]+[ \\s\\u00a0]*[.．][ \\s\\u00a0]*[^ \\s\\u00a0]+.*")) {
                phrase = phrase.substring(1 + phrase.split("[.．]")[0].length())
                        .replaceAll("[ \\s\\u00a0]+", " ")
                        .replaceAll("(^[ \\s\\u00a0]+)|([ \\s\\u00a0]+$)", "");
                phrases.add(phrase);
            }
        }
        Collection<Phrase> resultPhrases;
        if (!CollectionUtils.isEmpty(ref)) {
            resultPhrases = new ArrayList<>(ref);
            resultPhrases.removeIf(phrase -> !phrases.contains(phrase.getPrototype()));
        } else {
            resultPhrases = phraseService.findByPrototypes(phrases);
        }
        logger.info(name + "导入的短语：" + String.join(", ", resultPhrases.stream().map(Phrase::getPrototype).collect(Collectors.toList())));
        return resultPhrases;
    }

    private void internalImportPhrases(ProcessImportVo... processImportVos) {
        Student student = new Student();
        Group group = new Group();
        Set<Phrase> phrases = new HashSet<>();
        Arrays.sort(processImportVos, (processImportVo1, processImportVo2) -> {
            int result = 0;
            if (processImportVo1.getGroupId() != null && processImportVo2.getGroupId() != null) {
                result = processImportVo1.getGroupId().compareTo(processImportVo2.getGroupId());
            }
            if (processImportVo1.getGroupId() == null && processImportVo2.getGroupId() != null) {
                result = -1;
            }
            if (processImportVo1.getGroupId() != null && processImportVo2.getGroupId() == null) {
                result = 1;
            }
            if (result == 0) {
                result = processImportVo1.getStudentId().compareTo(processImportVo2.getStudentId());
            }
            return result;
        });

        for (ProcessImportVo processImportVo : processImportVos) {
            try {
                Integer groupId = processImportVo.getGroupId();
                Integer studentId = processImportVo.getStudentId();
                if (groupId == null) {
                    if (null == group.getId()) {
                        if (studentId.equals(student.getId())) {
                            phrases.addAll(getPhrases(processImportVo.getData(), processImportVo.getName(), group.getPhrases()));
                        } else {
                            if (null != student.getId()) {
                                studentService.addMasteredPhrases(student, phrases.toArray(new Phrase[0]));
                            }
                            student = studentService.find(studentId);
                            phrases = new HashSet<>(getPhrases(processImportVo.getData(), processImportVo.getName(), group.getPhrases()));
                        }
                    } else {
                        if (studentId.equals(student.getId())) {
                            phrases.addAll(getPhrases(processImportVo.getData(), processImportVo.getName(), group.getPhrases()));
                        } else {
                            if (null != student.getId()) {
                                this.scorePhrases(group, student, phrases.toArray(new Phrase[0]));
                            }
                            student = studentService.find(studentId);
                            phrases = new HashSet<>(getPhrases(processImportVo.getData(), processImportVo.getName(), group.getPhrases()));
                        }
                    }
                } else {
                    if (Objects.equals(group.getId(), groupId)) {
                        if (studentId.equals(student.getId())) {
                            phrases.addAll(getPhrases(processImportVo.getData(), processImportVo.getName(), group.getPhrases()));
                        } else {
                            if (null != student.getId()) {
                                this.scorePhrases(group, student, phrases.toArray(new Phrase[0]));
                            }
                            student = studentService.find(studentId);
                            phrases = new HashSet<>(getPhrases(processImportVo.getData(), processImportVo.getName(), group.getPhrases()));
                        }
                    } else {
                        if (null != student.getId()) {
                            if (null != group.getId()) {
                                this.scorePhrases(group, student, phrases.toArray(new Phrase[0]));
                            } else {
                                studentService.addMasteredPhrases(student, phrases.toArray(new Phrase[0]));
                            }
                        }
                        if (!studentId.equals(student.getId())) {
                            student = studentService.find(studentId);
                            Assert.notNull(student, "student不存在");
                        }
                        group = this.find(groupId);
                        Assert.notNull(group, "phrase group不存在");
                        phrases = new HashSet<>(getPhrases(processImportVo.getData(), processImportVo.getName(), group.getPhrases()));
                    }
                }
            } catch (Throwable e) {
                throw new ProcessImportException(processImportVo.getName(), e);
            }
        }
        if (null != student.getId()) {
            if (null != group.getId()) {
                this.scorePhrases(group, student, phrases.toArray(new Phrase[0]));
            } else {
                studentService.addMasteredPhrases(student, phrases.toArray(new Phrase[0]));
            }
        }
    }

    @Transactional
    @Override
    public void jump(Group group, Student student) {
        Assert.notNull(group, "group不能为null");
        Assert.notNull(student, "student不能为null");
        this.internalJump(group, student, null, null);
    }

    private void internalJump(Group group, Student student, Integer wordsCount, Integer phrasesCount) {
        if (group.getStudentsOriented().size() - group.getStudentsScored().size() == 1) {
            group.setStatus(COMPLETED);
            group.setScoreCount(group.getScoreCount() + 1);
        } else if (group.getStudentsScored().isEmpty()) {
            group.setStatus(SCORING);
        }
        if (group.getGroupLog() == null) {
            group.setGroupLog(new GroupLog());
            for (Word word : group.getWords()) {
                Word wordReview = new Word();
                wordReview.setId(word.getId());
                wordReview.setIndex(word.getIndex());
                wordReview.setPrototype(word.getPrototype());
                wordReview.setDefinition(word.getDefinition());
                wordReview.setMasteredStudentsCount(0);
                group.getGroupLog().getWords().add(wordReview);
            }
            for (Phrase phrase : group.getPhrases()) {
                Phrase phraseReview = new Phrase();
                phraseReview.setId(phrase.getId());
                phraseReview.setIndex(phrase.getIndex());
                phraseReview.setPrototype(phrase.getPrototype());
                phraseReview.setDefinition(phrase.getDefinition());
                phraseReview.setMasteredStudentsCount(0);
                group.getGroupLog().getPhrases().add(phraseReview);
            }
        }
        group.getStudentsScored().add(student);
        Student studentReview = new Student();
        studentReview.setId(student.getId());
        studentReview.setNumber(student.getNumber());
        studentReview.setName(student.getName());
        studentReview.setDescription(student.getDescription());
        Clazz clazz = new Clazz();
        clazz.setId(student.getClazz().getId());
        clazz.setName(student.getClazz().getName());
        clazz.setDescription(student.getClazz().getDescription());
        studentReview.setClazz(clazz);
        studentReview.setMasteredWordsCount(wordsCount);
        studentReview.setMasteredPhrasesCount(phrasesCount);
        group.getGroupLog().getStudents().add(studentReview);

        if (wordsCount != null || phrasesCount != null) {
            StudyLog studyLog = new StudyLog();
            studyLog.setGroupName(group.getName());
            studyLog.setGroupCreateDate(group.getCreateDate());
            studyLog.setGroupType(group.getType());
            studyLog.setLibraryName(group.getLibrary().getName());
            studyLog.setLogDate(new Date());
            studyLog.setStudent(student);
            studyLog.setWordsCount(group.getWordsCount());
            studyLog.setMasteredWordsCount(wordsCount);
            studyLog.setPhrasesCount(group.getPhrasesCount());
            studyLog.setMasteredPhrasesCount(phrasesCount);
            studyLogService.persist(studyLog);
        }
    }

    @Transactional
    @Override
    public void finish(Group group) {
        Assert.notNull(group, "group不能为null");
        group.getStudentsOriented().stream()
            .filter(student -> !group.getStudentsScored().contains(student))
            .forEach(student -> this.internalJump(group, student, null, null));
    }

    @Transactional
    @Override
    public void complete(Group group) {
        Assert.notNull(group, "group不能为null");
        group.setStatus(NORMAL);
        group.setGroupLog(null);
        group.getStudentsScored().clear();
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Student> getStudentsOriented(Group group) {
        return new HashSet<>(group.getStudentsOriented());
    }
}

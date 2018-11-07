package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.bean.Word;
import com.benzolamps.dict.controller.vo.ProcessImportVo;
import com.benzolamps.dict.exception.ProcessImportException;
import com.benzolamps.dict.service.base.WordGroupService;
import com.benzolamps.dict.util.DictQrCode;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 单词分组Service接口实现类
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-21 22:40:39
 */
@Slf4j
@Service("wordGroupService")
@Transactional
public class WordGroupServiceImpl extends GroupServiceImpl implements WordGroupService {

    protected WordGroupServiceImpl() {
        super(Group.Type.WORD);
    }

    @Override
    @Transactional
    public void addWords(Group wordGroup, Word... words) {
        Assert.notNull(wordGroup, "word group不能为null");
        Assert.noNullElements(words, "words不能存在为null的元素");
        wordGroup.getWords().addAll(Arrays.asList(words));
    }

    @Override
    @Transactional
    public void removeWords(Group wordGroup, Word... words) {
        Assert.notNull(wordGroup, "word group不能为null");
        Assert.noNullElements(words, "words不能存在为null的元素");
        List<Word> wordList = Arrays.asList(words);
        wordGroup.getWords().removeAll(wordList);
        if (wordGroup.getGroupLog() != null) {
            wordGroup.getGroupLog().getWords().removeAll(wordList);
        }

    }

    @Override
    @SuppressWarnings("ConstantConditions")
    @Transactional
    public void scoreWords(Group wordGroup, Student student, Word... words) {
        Assert.notNull(wordGroup, "word group不能为null");
        Assert.notNull(student, "student不能为null");
        Assert.notNull(words, "words不能为null");
        Assert.noNullElements(words, "words不能存在为null的元素");
        Assert.isTrue(!Group.Status.COMPLETED.equals(wordGroup.getStatus()), "该分组当前处于已完成状态，无法进行评分！");
        Assert.isTrue(wordGroup.getStudentsOriented().contains(student), "该学生不在此分组中");
        Assert.isTrue(!wordGroup.getStudentsScored().contains(student), "该学生已评分");
        studentService.addFailedWords(student, wordGroup.getWords().toArray(new Word[0]));
        studentService.addMasteredWords(student, words);
        this.internalJump(wordGroup, student, words.length, null);
        for (Word word : words) {
            Word wordReview = wordGroup.getGroupLog().getWords().stream().filter(word::equals).findFirst().orElse(null);
            wordReview.setMasteredStudentsCount(wordReview.getMasteredStudentsCount() + 1);
        }
    }

    @Override
    @Transactional
    public void importWords(ProcessImportVo... processImportVos) {
        Assert.notEmpty(processImportVos, "process import vos不能为null或空");
        for (ProcessImportVo processImportVo : processImportVos) {
            if (processImportVo.getStudentId() == null || processImportVo.getGroupId() == null) {
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
                    if (processImportVo.getStudentId() == null) {
                        processImportVo.setStudentId(studentId);
                    }
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
}

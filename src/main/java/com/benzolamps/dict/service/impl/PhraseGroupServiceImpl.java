package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.bean.Phrase;
import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.controller.vo.ProcessImportVo;
import com.benzolamps.dict.exception.DictException;
import com.benzolamps.dict.exception.ProcessImportException;
import com.benzolamps.dict.service.base.PhraseGroupService;
import com.benzolamps.dict.service.base.PhraseService;
import com.benzolamps.dict.util.DictQrCode;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 短语分组Service接口实现类
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-21 22:40:44
 */
@Slf4j
@Service("phraseGroupService")
@Transactional
public class PhraseGroupServiceImpl extends GroupServiceImpl implements PhraseGroupService {

    @Resource
    private PhraseService phraseService;

    protected PhraseGroupServiceImpl() {
        super(Group.Type.PHRASE);
    }

    @Override
    @Transactional
    public void addPhrases(Group phraseGroup, Phrase... phrases) {
        Assert.notNull(phraseGroup, "phrase group不能为null");
        Assert.noNullElements(phrases, "phrases不能存在为null的元素");
        phraseGroup.getPhrases().addAll(Arrays.asList(phrases));
    }

    @Override
    @Transactional
    public void removePhrases(Group phraseGroup, Phrase... phrases) {
        Assert.notNull(phraseGroup, "phrase group不能为null");
        Assert.noNullElements(phrases, "phrases不能存在为null的元素");
        phraseGroup.getPhrases().removeAll(Arrays.asList(phrases));
        List<Phrase> phraseList = Arrays.asList(phrases);
        phraseGroup.getPhrases().removeAll(phraseList);
        if (phraseGroup.getGroupLog() != null) {
            phraseGroup.getGroupLog().getPhrases().removeAll(phraseList);
        }
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    @Transactional
    public void scorePhrases(Group phraseGroup, Student student, Phrase... phrases) {
        Assert.notNull(phraseGroup, "phrase group不能为null");
        Assert.notNull(student, "student不能为null");
        Assert.notNull(phrases, "phrases不能为null");
        Assert.noNullElements(phrases, "phrases不能存在为null的元素");
        Assert.isTrue(!Group.Status.COMPLETED.equals(phraseGroup.getStatus()), phraseGroup.getName() + "分组当前处于已完成状态，无法进行评分！");
        Assert.isTrue(phraseGroup.getStudentsOriented().contains(student), student.getName() + "不在" + phraseGroup.getName() + "分组中！");
        Assert.isTrue(!phraseGroup.getStudentsScored().contains(student), student.getName() + "已评分" + phraseGroup.getName() + "！");
        studentService.addFailedPhrases(student, phraseGroup.getPhrases().toArray(new Phrase[0]));
        studentService.addMasteredPhrases(student, phrases);
        this.internalJump(phraseGroup, student, null, phrases.length);
        for (Phrase phrase : phrases) {
            Phrase phraseReview = phraseGroup.getGroupLog().getPhrases().stream().filter(phrase::equals).findFirst().orElse(null);
            phraseReview.setMasteredStudentsCount(phraseReview.getMasteredStudentsCount() + 1);
        }
    }

    @Override
    @Transactional
    public void importPhrases(ProcessImportVo... processImportVos) {
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
        internalImportPhrases(processImportVos);
    }

    private Collection<Phrase> getPhrases(byte[] data, String name, Collection<Phrase> ref) {
        List<String> phrases = new ArrayList<>();
        JSONObject res = aipProperties.basicAccurateGeneral(data);
        Object errorCode = res.opt("error_code");
        if (errorCode != null) {
            throw new DictException(errorCode + "：" + res.get("error_msg"));
        }
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
        logger.info(name + "导入 " + resultPhrases.size() + " 个短语：" + String.join(", ", resultPhrases.stream().map(Phrase::getPrototype).collect(Collectors.toList())));
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
                            if (group.getId() != null) {
                                Assert.isTrue(!Group.Status.COMPLETED.equals(group.getStatus()), group.getName() + "分组当前处于已完成状态，无法进行评分！");
                                Assert.isTrue(group.getStudentsOriented().contains(student), student.getName() + "不在" + group.getName() + "分组中！");
                                Assert.isTrue(!group.getStudentsScored().contains(student), student.getName() + "已评分" + group.getName() + "！");
                            }
                        }
                        group = this.find(groupId);
                        Assert.notNull(group, "phrase group不存在");
                        Assert.isTrue(!Group.Status.COMPLETED.equals(group.getStatus()), group.getName() + "分组当前处于已完成状态，无法进行评分！");
                        Assert.isTrue(group.getStudentsOriented().contains(student), student.getName() + "不在" + group.getName() + "分组中！");
                        Assert.isTrue(!group.getStudentsScored().contains(student), student.getName() + "已评分" + group.getName() + "！");
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
}

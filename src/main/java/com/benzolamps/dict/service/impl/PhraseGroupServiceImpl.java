package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.bean.Phrase;
import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.controller.vo.ProcessImportVo;
import com.benzolamps.dict.service.base.PhraseGroupService;
import com.benzolamps.dict.service.base.PhraseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

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
        this.assertGroupAndStudent(phraseGroup, student);
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
        throw new UnsupportedOperationException();
    }
}

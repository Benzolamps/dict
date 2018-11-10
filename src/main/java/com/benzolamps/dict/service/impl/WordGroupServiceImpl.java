package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.bean.Word;
import com.benzolamps.dict.controller.vo.ProcessImportVo;
import com.benzolamps.dict.exception.ProcessImportException;
import com.benzolamps.dict.service.base.WordGroupService;
import com.benzolamps.dict.service.base.WordService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
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

    @Resource
    private WordService wordService;

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
        Assert.notNull(words, "words不能为null");
        Assert.noNullElements(words, "words不能存在为null的元素");
        this.assertGroupAndStudent(wordGroup, student);
        studentService.addFailedWords(student, wordGroup.getWords().toArray(new Word[0]));
        studentService.addMasteredWords(student, words);
        this.internalJump(wordGroup, student, words.length, null);
        for (Word word : words) {
            Word wordReview = wordGroup.getGroupLog().getWords().stream().filter(word::equals).findFirst().orElse(null);
            wordReview.setMasteredStudentsCount(wordReview.getMasteredStudentsCount() + 1);
        }
    }

    @SuppressWarnings({"unchecked", "StatementWithEmptyBody"})
    @Override
    @Transactional
    @SneakyThrows
    public void importWords(ProcessImportVo... processImportVos) {
        Assert.notEmpty(processImportVos, "process import vos不能为null或空");
        AtomicReference<Throwable> throwable = new AtomicReference<>();
        Collection<Thread> threads = Arrays.stream(processImportVos)
            .map(GroupQrCodeThread::new)
            .collect(Collectors.toList());
        while (throwable.get() == null && threads.stream().noneMatch(Thread::isInterrupted)) {
            long count = threads.stream().filter(Thread::isAlive).count();
            if (count < 5) {
                threads.stream().filter(thread -> !thread.isInterrupted()).limit(5 - count).forEach(Thread::start);
            }
        }
        if (throwable.get() != null) {
            threads.forEach(Thread::interrupt);
            throw throwable.get();
        }
        internalImportWords(processImportVos);
    }

    private void handle(ProcessImportVo... processImportVos) {
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
        Arrays.stream(processImportVos).forEach(processImportVo -> {
            try {
                if (processImportVo.getStudentId() != null) {
                    processImportVo.setStudent(studentService.find(processImportVo.getStudentId()));
                }
                if (processImportVo.getGroupId() != null) {
                    processImportVo.setGroup(this.find(processImportVo.getGroupId()));
                    this.assertGroupAndStudent(processImportVo.getGroup(), processImportVo.getStudent());
                }
            } catch (Throwable e) {
                throw new ProcessImportException(processImportVo.getName(), null, e);
            }
        });
    }

    private Collection<Word> getWords(ProcessImportVo processImportVo, Collection<Word> ref) {
        Collection<Word> resultWords;
        if (!CollectionUtils.isEmpty(ref)) {
            resultWords = new ArrayList<>(ref);
            resultWords.removeIf(word -> !processImportVo.getWords().contains(word.getPrototype().toLowerCase()));
        } else {
            resultWords = wordService.findByPrototypes(processImportVo.getWords());
        }
        logger.info(
            processImportVo.getName() + " 导入 " + resultWords.size() + " 个单词：" +
            String.join(", ", resultWords.stream().map(Word::getPrototype).collect(Collectors.toList()))
        );
        return resultWords;
    }

    @SneakyThrows
    @SuppressWarnings({"unchecked", "StatementWithEmptyBody"})
    private void internalImportWords(ProcessImportVo... processImportVos) {
        handle(processImportVos);
        AtomicReference<Throwable> throwable = new AtomicReference<>();
        Collection<Thread> threads = Arrays.stream(processImportVos)
            .map(GroupQrCodeThread::new)
            .collect(Collectors.toList());
        while (throwable.get() == null && threads.stream().noneMatch(Thread::isInterrupted)) {
            long count = threads.stream().filter(Thread::isAlive).count();
            if (count < 5) {
                threads.stream().filter(thread -> !thread.isInterrupted()).limit(5 - count).forEach(Thread::start);
            }
        }
        if (throwable.get() != null) {
            threads.forEach(Thread::interrupt);
            throw throwable.get();
        }
        Set<Word> words = new HashSet<>();
        ProcessImportVo curr = new ProcessImportVo(null, null, null, null);
        for (ProcessImportVo piv : processImportVos) {
            if (curr.getGroup().equals(piv.getGroup()) && curr.getStudent().equals(piv.getStudent())) {
                words.addAll(getWords(piv, piv.getGroup().getWords()));
            } else {
                if (null != curr.getStudent().getId()) {
                    if (curr.getGroup().getId() != null) {
                        this.scoreWords(curr.getGroup(), curr.getStudent(), words.toArray(new Word[0]));
                    } else {
                        studentService.addMasteredWords(curr.getStudent(), words.toArray(new Word[0]));
                    }
                }
                words = new HashSet<>(getWords(piv, piv.getGroup().getWords()));

            }
            curr = piv;
        }
        if (null != curr.getStudent().getId()) {
            if (curr.getGroup().getId() != null) {
                this.scoreWords(curr.getGroup(), curr.getStudent(), words.toArray(new Word[0]));
            } else {
                studentService.addMasteredWords(curr.getStudent(), words.toArray(new Word[0]));
            }
        }
    }
}

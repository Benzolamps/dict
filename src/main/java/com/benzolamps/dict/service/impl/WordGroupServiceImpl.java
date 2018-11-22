package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.FrequencyInfo;
import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.bean.Student;
import com.benzolamps.dict.bean.Word;
import com.benzolamps.dict.controller.vo.ProcessImportVo;
import com.benzolamps.dict.exception.ProcessImportException;
import com.benzolamps.dict.service.base.WordGroupService;
import com.benzolamps.dict.service.base.WordService;
import com.benzolamps.dict.util.Constant;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.POITextExtractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Assert.isTrue(!wordGroup.getFrequencyGenerated(), "词频分组不能添加单词");
        Assert.noNullElements(words, "words不能存在为null的元素");
        wordGroup.getWords().addAll(Arrays.asList(words));
    }

    @Override
    @Transactional
    public void removeWords(Group wordGroup, Word... words) {
        Assert.notNull(wordGroup, "word group不能为null");
        Assert.noNullElements(words, "words不能存在为null的元素");
        List<Word> wordList = Arrays.asList(words);
        if (wordGroup.getFrequencyGenerated()) {
            wordList.stream().map(Word::getFrequencyInfo).forEach(infos -> infos.removeIf(info -> info.getGroupId().equals(wordGroup.getId())));
        }
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
        this.assertGroupAndStudent(wordGroup, student);
        studentService.addFailedWords(student, wordGroup.getWords().toArray(new Word[0]));
        studentService.addMasteredWords(student, words);
        this.internalJump(wordGroup, student, words.length, null);
        for (Word word : words) {
            Word wordReview = wordGroup.getGroupLog().getWords().stream().filter(word::equals).findFirst().orElse(null);
            wordReview.setMasteredStudentsCount(wordReview.getMasteredStudentsCount() + 1);
        }
    }

    private ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setMaxPoolSize(5);
        threadPoolTaskExecutor.setCorePoolSize(5);
        threadPoolTaskExecutor.setQueueCapacity(9999);
        threadPoolTaskExecutor.setThreadNamePrefix("process-import-");
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    @SuppressWarnings({"StatementWithEmptyBody", "ConstantConditions"})
    @Override
    @Transactional
    @SneakyThrows
    public void importWords(ProcessImportVo... processImportVos) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = this.getThreadPoolTaskExecutor();
        Assert.notEmpty(processImportVos, "process import vos不能为null或空");
        AtomicReference<Throwable> throwable = new AtomicReference<>();
        Stream.of(processImportVos)
            .map(GroupQrCodeTask::new)
            .forEach(task -> {
                ListenableFuture<?> listenableFuture = threadPoolTaskExecutor.submitListenable(task);
                listenableFuture.addCallback(result -> {}, e -> {
                    throwable.set(e);
                    threadPoolTaskExecutor.shutdown();
                    threadPoolTaskExecutor.initialize();
                });
            });
        while (throwable.get() == null && threadPoolTaskExecutor.getThreadPoolExecutor().getCompletedTaskCount() < threadPoolTaskExecutor.getThreadPoolExecutor().getTaskCount()) {
            Thread.sleep(100);
        }
        logger.info("识别二维码完成");
        if (throwable.get() != null) {
            throw throwable.get();
        }
        internalImportWords(processImportVos);
    }

    @Override
    public Group extractDeriveGroup(Group original, Collection<Word> words, Collection<Student> students, Group wordGroup) {
        Assert.isTrue(!(CollectionUtils.isEmpty(words) && CollectionUtils.isEmpty(students)), "words和students不能同时为null或空");
        /* lambda只能用final */
        Collection<?> words1 = words, students1 = students;
        /* 去除所有学生都会的单词 */
        if (CollectionUtils.isEmpty(words)) {
            Assert.notNull(original, "original不能为null");
            words = new HashSet<>(original.getWords());
            words.removeIf(word -> word.getMasteredStudents().containsAll(students1));
        }
        /* 去除所有单词都会的学生 */
        if (CollectionUtils.isEmpty(students)) {
            Assert.notNull(original, "original不能为null");
            students = new HashSet<>(original.getStudentsOriented());
            students.removeIf(student -> student.getMasteredWords().containsAll(words1));
        }
        wordGroup.setWords(new HashSet<>(words));
        wordGroup.setStudentsOriented(new HashSet<>(students));
        return persist(wordGroup);
    }

    @Override
    public Collection<Group> extractPersonalGroup(Group original, Collection<Student> students, Group wordGroup) {
        Assert.notEmpty(students, "students不能为空");
        Collection<Group> groups = students.stream().map(student -> {
           Group group = new Group();
           String name = wordGroup.getName() + " - " + student.getName();
           int i = 0;
           while (nameExists(name)) {
               name = wordGroup.getName() + " - " + student.getName() + " - " + i++;
           }
           group.setName(name);
           group.setDescription(wordGroup.getDescription());
           Set<Word> words = original != null ? original.getWords() : student.getFailedWords().stream().filter(word -> word.getLibrary().equals(assertLibrary())).collect(Collectors.toSet());
           words.removeIf(student.getMasteredWords()::contains);
           group.setWords(words);
           group.setStudentsOriented(Collections.singleton(student));
           return group;
        }).collect(Collectors.toList());
        persist(groups);
        return groups;
    }

    private void handle(ProcessImportVo... processImportVos) {
        Arrays.sort(processImportVos, (piv1, piv2) -> {
            int result = 0;
            if (piv1.getGroupId() != null && piv2.getGroupId() != null) {
                result = piv1.getGroupId().compareTo(piv2.getGroupId());
            }
            if (piv1.getGroupId() == null && piv2.getGroupId() != null) {
                result = -1;
            }
            if (piv1.getGroupId() != null && piv2.getGroupId() == null) {
                result = 1;
            }
            if (result == 0) {
                result = piv1.getStudentId().compareTo(piv2.getStudentId());
            }
            return result;
        });
        Stream.of(processImportVos).forEach(processImportVo -> {
            try {
                if (processImportVo.getStudentId() != null) {
                    processImportVo.setStudent(studentService.find(processImportVo.getStudentId()));
                    Assert.notNull(processImportVo.getStudent(), "student不存在");
                }
                if (processImportVo.getGroupId() != null) {
                    processImportVo.setGroup(this.find(processImportVo.getGroupId()));
                    Assert.notNull(processImportVo.getGroup(), "word group不存在");
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
        ThreadPoolTaskExecutor threadPoolTaskExecutor = this.getThreadPoolTaskExecutor();
        AtomicReference<Throwable> throwable = new AtomicReference<>();
        Stream.of(processImportVos)
            .map(GroupBaseElementTask::new)
            .forEach(task -> {
                ListenableFuture<?> listenableFuture = threadPoolTaskExecutor.submitListenable(task);
                listenableFuture.addCallback(result -> {}, e -> {
                    throwable.set(e);
                    threadPoolTaskExecutor.shutdown();
                    threadPoolTaskExecutor.initialize();
                });
            });
        while (throwable.get() == null && threadPoolTaskExecutor.getThreadPoolExecutor().getCompletedTaskCount() < threadPoolTaskExecutor.getThreadPoolExecutor().getTaskCount()) {
            Thread.sleep(100);
        }
        if (throwable.get() != null) {
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


    private void handleFrequencyGroup(Group wordGroup,String content, List<String> extraWords) {
        String regex = "[A-Za-z]+";
        Matcher matcher = Pattern.compile(regex).matcher(content);
        Map<String, Integer> frequencies = new Hashtable<>();
        while (matcher.find()) {
            String word = matcher.group().toLowerCase();
            frequencies.put(word, frequencies.containsKey(word) ? frequencies.get(word) + 1 : 1);
        }
        wordGroup.setFrequencyGenerated(true);
        Collection<Word> words = wordService.findByPrototypes(frequencies.keySet());
        for (Word word : words) {
            word.getFrequencyInfo().add(new FrequencyInfo(wordGroup.getId(), frequencies.get(word.getPrototype().toLowerCase())));
            word.setFrequency(word.getFrequencyInfo().stream().mapToInt(FrequencyInfo::getFrequency).sum());
        }
        extraWords.addAll(frequencies.keySet());
        extraWords.removeAll(words.stream().map(Word::getPrototype).collect(Collectors.toList()));
        wordGroup.setWords(new HashSet<>(words));
    }

    @Transactional
    @Override
    public Group persistFrequencyGroupStr(Group wordGroup, String content, List<String> extraWords) {
        wordGroup = persist(wordGroup);
        handleFrequencyGroup(wordGroup, content, extraWords);
        return wordGroup;
    }

    @Transactional
    @Override
    public Group persistFrequencyGroupTxt(Group wordGroup, byte[] bytes, List<String> extraWords) {
        String content = new String(bytes, Constant.UTF8_CHARSET);
        return persistFrequencyGroupStr(wordGroup, content, extraWords);
    }

    @Transactional
    @Override
    @SneakyThrows(IOException.class)
    public Group persistFrequencyGroupDoc(Group wordGroup, byte[] bytes, List<String> extraWords) {
        POITextExtractor extractor;
        try {
            extractor = new WordExtractor(new ByteArrayInputStream(bytes));
        } catch (OfficeXmlFileException e) {
            extractor = new XWPFWordExtractor(new XWPFDocument(new ByteArrayInputStream(bytes)));
        }
        String content = extractor.getText();
        return persistFrequencyGroupStr(wordGroup, content, extraWords);
    }

    @Transactional
    @Override
    public Group updateFrequencyGroupStr(Group wordGroup, String content, List<String> extraWords) {
        wordGroup = find(wordGroup.getId());
        Assert.isTrue(wordGroup.getFrequencyGenerated(), "word group不是词频分组");
        clearFrequencyInfo(wordGroup);
        handleFrequencyGroup(wordGroup, content, extraWords);
        return wordGroup;
    }

    @Transactional
    @Override
    public Group updateFrequencyGroupTxt(Group wordGroup, byte[] bytes, List<String> extraWords) {
        String content = new String(bytes, Constant.UTF8_CHARSET);
        return updateFrequencyGroupStr(wordGroup, content, extraWords);
    }

    @Transactional
    @Override
    @SneakyThrows(IOException.class)
    public Group updateFrequencyGroupDoc(Group wordGroup, byte[] bytes, List<String> extraWords) {
        POITextExtractor extractor;
        try {
            extractor = new WordExtractor(new ByteArrayInputStream(bytes));
        } catch (OfficeXmlFileException e) {
            extractor = new XWPFWordExtractor(new XWPFDocument(new ByteArrayInputStream(bytes)));
        }
        String content = extractor.getText();
        return updateFrequencyGroupStr(wordGroup, content, extraWords);
    }
}

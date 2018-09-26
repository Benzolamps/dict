package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.*;
import com.benzolamps.dict.bean.Group.Status;
import com.benzolamps.dict.bean.Group.Type;
import com.benzolamps.dict.dao.base.GroupDao;
import com.benzolamps.dict.dao.core.Filter;
import com.benzolamps.dict.service.base.GroupService;
import com.benzolamps.dict.util.DictArray;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 单词短语分组Service接口实现类
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-21 22:09:55
 */
public abstract class GroupServiceImpl extends BaseServiceImpl<Group> implements GroupService {

    private final Type type;

    @Resource
    private GroupDao groupDao;

    protected GroupServiceImpl(Type type) {
        Assert.notNull(type, "type不能为null");
        this.type = type;
    }

    @Override
    public Group find(Integer id) {
        Group group = super.find(id);
        return type.equals(group.getType()) ? group : null;
    }

    @Override
    public void persist(Group... groups) {
        Arrays.stream(groups).forEach(group -> {
            group.setType(type);
            group.setStatus(Status.NORMAL);
        });
        super.persist(groups);
    }

    @Override
    public Group persist(Group group) {
        group.setType(type);
        group.setStatus(Status.NORMAL);
        return super.persist(group);
    }

    @Override
    public void persist(Collection<Group> groups) {
        groups.forEach(group -> {
            group.setType(type);
            group.setStatus(Status.NORMAL);
        });
        super.persist(groups);
    }

    @Override
    public Group update(Group group, String... ignoreProperties) {
        return super.update(group, DictArray.add(ignoreProperties, "type"));
    }

    @Override
    public void update(Collection<Group> groups, String... ignoreProperties) {
        super.update(groups, DictArray.add(ignoreProperties, "type"));
    }

    @Override
    protected void handlerFilter(Filter filter) {
        filter.and(Filter.eq("type", type));
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
        group.getStudentsOriented().addAll(Arrays.stream(students).collect(Collectors.toSet()));
    }

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

    public void addWords(Group wordGroup, Word... words) {
        Assert.notNull(wordGroup, "word group不能为null");
        Assert.noNullElements(words, "words不能存在为null的元素");
        Assert.isTrue(type == Type.WORD && wordGroup.getType() == type, "group类型错误");
        wordGroup.getWords().addAll(Arrays.stream(words).collect(Collectors.toSet()));
    }

    public void addPhrases(Group phraseGroup, Phrase... phrases) {
        Assert.notNull(phraseGroup, "phrase group不能为null");
        Assert.noNullElements(phrases, "phrases不能存在为null的元素");
        Assert.isTrue(type == Type.PHRASE && phraseGroup.getType() == type, "group类型错误");
        phraseGroup.getPhrases().addAll(Arrays.stream(phrases).collect(Collectors.toSet()));
    }
}

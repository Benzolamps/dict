package com.benzolamps.dict.controller.vo;

import com.benzolamps.dict.bean.*;
import com.benzolamps.dict.service.base.BaseElementService;
import com.benzolamps.dict.service.base.GroupService;
import com.benzolamps.dict.service.base.StudentService;
import com.benzolamps.dict.util.DictArray;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 创建分组Vo
 * @author Benzolamps
 * @version 2.3.1
 * @datetime 2018-11-16 20:59:56
 */
@SuppressWarnings("unchecked")
@RequiredArgsConstructor
public class ExtractGroupVo<T extends BaseElement> implements Serializable {
    private static final long serialVersionUID = -3104948090003565239L;

    private final Integer groupId;

    private final Integer[] elementIds;

    private final Integer[] studentIds;

    private final GroupService groupService;

    private final BaseElementService<T> elementService;

    private final StudentService studentService;

    /**
     * 获取分组
     * @return 分组
     */
    public Group getGroup() {
        return groupService.find(groupId);
    }

    /**
     * 获取元素集合
     * @return 元素集合
     */
    @SuppressWarnings("ConstantConditions")
    public Collection<T> getElements() {
        if (ObjectUtils.isEmpty(elementIds)) {
            return null;
        }
        Group group = getGroup();
        if (group == null) {
            return Stream.of(elementIds).map(elementService::find).collect(Collectors.toList());
        } else {
            return (Collection<T>) group.getElements().stream().filter(element -> DictArray.contains(elementIds, element.getId())).collect(Collectors.toList());
        }
    }

    /**
     * 获取单词集合
     * @return 元素集合
     */
    @SuppressWarnings("ConstantConditions")
    public Collection<Word> getWords() {
        return (Collection<Word>) getElements();
    }

    /**
     * 获取短语集合
     * @return 元素集合
     */
    @SuppressWarnings("ConstantConditions")
    public Collection<Phrase> getPhrases() {
        return (Collection<Phrase>) getElements();
    }

    /**
     * 获取学生集合
     * @return 学生集合
     */
    @SuppressWarnings("ConstantConditions")
    public Collection<Student> getStudents() {
        if (ObjectUtils.isEmpty(studentIds)) {
            return null;
        }
        Group group = getGroup();
        if (group == null) {
            return Stream.of(studentIds).map(studentService::find).collect(Collectors.toList());
        } else {
            return group.getStudentsOriented().stream().filter(student -> DictArray.contains(studentIds, student.getId())).collect(Collectors.toList());
        }
    }

}

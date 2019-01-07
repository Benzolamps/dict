package com.benzolamps.dict.controller.vo;

import com.benzolamps.dict.bean.BaseElement;
import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.bean.Student;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * 单词或短语排序
 * @param <E> 单词或短语
 * @author Benzolamps
 * @version 2.3.5
 * @datetime 2018-11-28 13:36:27
 */
@RequiredArgsConstructor
public class BaseElementComparator<E extends BaseElement> implements Comparator<E>, Serializable {

    private static final long serialVersionUID = -484680822558494553L;

    private final Group group;

    private final int compareStrategy;

    /**
     * 构造器
     * @param compareStrategy 比较策略
     */
    public BaseElementComparator(int compareStrategy) {
        this(null, compareStrategy);
    }

    /**
     * 按照索引
     */
    public static final int INDEX = 0;

    /**
     * 按照原形
     */
    public static final int PROTOTYPE = 1;

    /**
     * 按照已掌握的学生数
     */
    public static final int MASTERED_STUDENTS_COUNT = 2;

    /**
     * 按照未掌握的学生数
     */
    public static final int FAILED_STUDENTS_COUNT = 3;

    /**
     * 按照词频
     */
    public static final int FREQUENCY = 4;

    @Override
    public int compare(E element1, E element2) {
        if (compareStrategy > FREQUENCY) {
            E temp = element2;
            element2 = element1;
            element1 = temp;
        }
        switch (compareStrategy % (FREQUENCY + 1)) {
            case INDEX:
                return element1.getIndex().compareTo(element2.getIndex());
            case PROTOTYPE:
                return element1.getPrototype().compareToIgnoreCase(element2.getPrototype());
            case MASTERED_STUDENTS_COUNT: {
                List<Student> students1 = new ArrayList<>(element1.getMasteredStudents());
                if (null != group) students1.retainAll(group.getStudentsOriented());
                List<Student> students2 = new ArrayList<>(element2.getMasteredStudents());
                if (null != group) students2.retainAll(group.getStudentsOriented());
                return students1.size() - students2.size();
            }
            case FAILED_STUDENTS_COUNT: {
                List<Student> students1 = new ArrayList<>(element1.getFailedStudents());
                if (null != group) students1.retainAll(group.getStudentsOriented());
                List<Student> students2 = new ArrayList<>(element2.getFailedStudents());
                if (null != group) students2.retainAll(group.getStudentsOriented());
                return students1.size() - students2.size();
            }
            case FREQUENCY: {
                if (null != group && group.getFrequencyGenerated()) {
                    Collection<? extends BaseElement> frequencySortedElements = group.getFrequencySortedElements();
                    return element1.getGroupFrequency() - element2.getGroupFrequency();
                } else {
                    return element1.getFrequency() - element2.getFrequency();
                }
            }
            default:
                return element1.getId().compareTo(element2.getId());
        }
    }
}

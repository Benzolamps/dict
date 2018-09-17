package com.benzolamps.dict.bean;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * 单词分组或短语分组的基类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-15 14:29:16
 */
@MappedSuperclass
@Getter
@Setter
public class BaseGroup extends BaseEntity {

    private static final long serialVersionUID = -756587292984332161L;

    /** 分组 */
    @ManyToOne
    @JoinColumn(updatable = false)
    private Library library;

    /** 状态 */
    public enum Status {
        /** 正常状态, 此状态可以导出单词, 可以删除, 可以给学生评分, 若已有评分, 则自动进入SCORING状态 */
        NORMAL,

        /**
         * 评分状态, 此状态可以导出单词, 但不可删除, 可以终止评分, 进入COMPLETED状态
         * 终止后未评分的学生的单词将不会做改动, 已评分才会做改动,
         * 评分过的学生评分记录会被保留, 不能再次评分
         * 所有学生全部评分完毕后, 自动进入COMPLETED状态
         */
        SCORING,

        /**
         * 已完成状态, 可以导出, 也可以删除, 可以查看单词与学生掌握情况
         * 可以转换为正常状态, 所有学生都可以重新评分
         */
        COMPLETED
    }

    /** 状态 */
    @Column(nullable = false)
    private Status status;
}

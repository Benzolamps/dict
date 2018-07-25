package com.benzolamps.dict.util;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 对一个列表进行乱序且重复
 * @param <E>
 * @author Benzolamps
 * @version 1.1.2
 * @datetime 2018-6-18 15:25:17
 */
@SuppressWarnings("unused")
public class Shuffler<E> extends Repeater<E> {

    /* 乱序基于的随机数类对象 */
    private Random random;

    /**
     * 构造器, 以时间戳作为随机数种子
     * @param list 列表
     * @param repeat 重复次数
     */
    public Shuffler(List<E> list, int repeat) {
        this(list, new Random(), repeat);
    }

    /**
     * 构造器, 以时间戳作为随机数种子, 无限重复
     * @param list 列表
     */
    public Shuffler(List<E> list) {
        this(list, new Random(), -1);
    }

    /**
     * 构造器, 无限重复
     * @param list 列表
     * @param random 随机数对象
     */
    public Shuffler(List<E> list, Random random) {
        this(list, random, -1);
    }

    /**
     * 构造器
     * @param list 列表
     * @param random 随机数对象
     * @param repeat 重复次数
     */
    public Shuffler(List<E> list, Random random, int repeat) {
        super(list, repeat);
        this.random = random;
    }

    @Override
    protected void beforeRepeat(List<E> list) {
        super.beforeRepeat(list);
        Collections.shuffle(list, random);
    }
}

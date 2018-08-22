package com.benzolamps.dict.util;

import org.springframework.util.Assert;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * 对一个列表进行重复
 * @param <E> 元素类型
 * @author Benzolamps
 * @version 1.1.2
 * @datetime 2018-6-18 15:19:27
 */
@SuppressWarnings({"deprecation", "unused"})
public class Repeater<E> implements Iterator<E> {

    /* 列表 */
    private List<E> list;

    /* 迭代器 */
    private Iterator<E> iterator;

    /* 重复的次数 */
    private int repeat;

    /* 当前已重复次数 */
    private int curRepeat = 0;

    /**
     * 构造器
     * @param list 列表
     * @param repeat 重复的次数, 小于 0 则为无限重复
     */
    public Repeater(List<E> list, int repeat) {
        Assert.notEmpty(list);
        this.list = list;
        this.repeat = repeat;
    }

    /**
     * 构造器, 无限重复
     * @param list 列表
     */
    public Repeater(List<E> list) {
        this(list, -1);
    }

    /**
     * 在下一次重复之前调用
     * @param list list
     */
    protected void beforeRepeat(List<E> list) {
    }

    @Override
    public final boolean hasNext() {
        if (iterator == null || !iterator.hasNext()) {
            beforeRepeat(list);
            iterator = list.iterator();
            curRepeat++;
        }

        return repeat < 0 || repeat != 0 && curRepeat <= repeat;
    }

    @Override
    public E next() {
        Assert.notEmpty(list);
        if (hasNext()) return iterator.next();
        else throw new NoSuchElementException();
    }
}

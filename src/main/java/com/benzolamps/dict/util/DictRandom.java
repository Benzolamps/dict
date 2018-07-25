package com.benzolamps.dict.util;

import java.util.Random;

/**
 * 自定义随机数类, 实现两个数决定一个随机数种子
 * @author Benzolamps
 * @version 1.1.2
 * @datetime 2018-6-18 15:16:32
 */
@SuppressWarnings({"serial", "unused"})
public class DictRandom extends Random {

    /* 辅助随机数类 */
    private Random random;

    /**
     * 构造器
     * @param seed1 第一个种子
     * @param seed2 第二个种子
     */
    public DictRandom(long seed1, long seed2) {
        super(seed1);
        random = new Random(seed2);
    }

    /*
     * 自定义随机数算法
     * 直到两个生成的随机数相等时, 生成的下个随机数即符合条件
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public int nextInt(int bounds) {
        while (super.nextInt(bounds) != random.nextInt(bounds));
        return super.nextInt(bounds);
    }
}

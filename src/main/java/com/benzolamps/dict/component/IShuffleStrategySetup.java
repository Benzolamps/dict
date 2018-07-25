package com.benzolamps.dict.component;

import java.io.Serializable;

/**
 * 抄写表生成策略配置接口
 * @author Benzolamps
 * @version 1.1.2
 * @datetime 2018-6-18 15:46:04
 */
@FunctionalInterface
public interface IShuffleStrategySetup extends Serializable {

    /**
     * 配置
     * @param size 表示要乱序的序列的总容量
     * @param hash 哈希值
     * @return 抄写表生成器
     */
    IShuffleStrategy setup(int size, int hash);
}

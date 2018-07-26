package com.benzolamps.dict.component;

import java.io.Serializable;
import java.util.Iterator;

/**
 * 抄写表生成策略接口
 * @author Benzolamps
 * @version 1.1.2
 * @datetime 2018-6-18 15:45:46
 */
public interface IShuffleStrategy extends Iterator<Integer>, Serializable {

    /** @return 当前序号是否可见 */
    boolean visible();
}

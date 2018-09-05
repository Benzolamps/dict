package com.benzolamps.dict.controller.vo;

import com.benzolamps.dict.bean.BaseElement;

import java.io.Serializable;

/**
 * 单词或短语Vo接口
 * @param <T>
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-5 21:13:00
 */
public interface BaseElementVo<T extends BaseElement> extends Serializable {

    /**
     * 将Vo转换为实体类对象
     * @return 实体类对象
     */
    T convertToElement();
}

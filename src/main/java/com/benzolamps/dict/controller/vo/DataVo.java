package com.benzolamps.dict.controller.vo;

import lombok.Getter;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * 表格数据Vo
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-22 23:14:56
 */
@Getter
public class DataVo extends BaseVo {

    private static final long serialVersionUID = -5845261821970423570L;

    /** 数据总量 */
    private final Long count;

    /** 数据 */
    private final Object data;

    /**
     * 构造器
     * @param data 数据
     */
    public DataVo(Object data) {
        super(200, null);
        this.data = data;
        if (data == null) {
            this.count = null;
        } else if (data.getClass().isArray()) {
            this.count = (long) Array.getLength(data);
        }
        else if (Collection.class.isAssignableFrom(data.getClass())) {
            this.count = (long) ((Collection<?>) data).size();
        } else {
            this.count = null;
        }
    }
}

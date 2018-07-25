package com.benzolamps.dict.controller.vo;

import com.benzolamps.dict.dao.core.Page;
import lombok.Getter;

import java.util.Collection;
import java.util.HashSet;

/**
 * 表格数据Vo
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-22 23:14:56
 */
@Getter
public class DataVo extends BaseVo {

    private static final long serialVersionUID = -5845261821970423570L;

    /** 状态码 */
    private final Byte code;

    /** 提示信息 */
    private final String msg;

    /** 数据总量 */
    private final Long count;

    /** 数据 */
    private final Collection data;

    /**
     * 构造器
     * @param data 数据
     */
    public DataVo(Page data) {
        this.count = (long) data.getContent().size();
        this.data = data.getContent();
        this.code = 0;
        this.msg = "";
    }

    /**
     * 构造器
     * @param msg 提示信息
     * @param code 状态码
     */
    public DataVo(String msg, Byte code) {
        this.msg = msg;
        this.code = code;
        this.data = new HashSet();
        this.count = 0L;
    }
}

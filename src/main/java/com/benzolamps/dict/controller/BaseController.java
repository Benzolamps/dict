package com.benzolamps.dict.controller;

import com.benzolamps.dict.controller.vo.DataVo;
import com.benzolamps.dict.dao.core.Page;

import java.util.Collection;

/**
 * 控制器基类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-4 20:51:17
 */
public class BaseController {

    /**
     * 转换数据
     * @param page 集合
     * @return DataVo
     */
    protected DataVo wrapperData(Page page) {
        return new DataVo(page);
    }

    /**
     * 转换数据
     * @param msg 消息
     * @param code 状态码
     * @return DataVo
     */
    protected DataVo wrapperMsg(String msg, Byte code) {
        return new DataVo(msg, code);
    }
}

package com.benzolamps.dict.controller.vo;

import com.sun.xml.internal.rngom.parse.host.Base;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * Vo基类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-25 20:11:08
 */
@Getter
@AllArgsConstructor
public class BaseVo implements Serializable {

    private static final long serialVersionUID = 765352179825632965L;

    /** 状态码 */
    private final Integer status;

    /** 提示信息 */
    private final String message;

    public static final BaseVo SUCCESS_VO = new BaseVo(200, "success");
}

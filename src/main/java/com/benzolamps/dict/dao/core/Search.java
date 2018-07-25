package com.benzolamps.dict.dao.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 搜索
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-25 19:57:02
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Search implements Serializable {

    private static final long serialVersionUID = 3930430224720429109L;

    /** 搜索字段 */
    private String field;

    /** 搜索值 */
    private Object value;
}

package com.benzolamps.dict.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 学习进度导入Vo
 * @author Benzolamps
 * @version 2.2.3
 * @datetime 2018-11-7 12:43:36
 */
@Data
@AllArgsConstructor
public class ProcessImportVo implements Serializable {

    private static final long serialVersionUID = 7848805671649593049L;

    private Integer groupId;

    private Integer studentId;

    private String name;

    private byte[] data;
}

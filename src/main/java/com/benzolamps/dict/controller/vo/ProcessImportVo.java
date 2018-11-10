package com.benzolamps.dict.controller.vo;

import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.bean.Student;
import lombok.Data;

import java.io.Serializable;
import java.util.Collection;

/**
 * 学习进度导入Vo
 * @author Benzolamps
 * @version 2.2.3
 * @datetime 2018-11-7 12:43:36
 */
@Data
public class ProcessImportVo implements Serializable {

    private static final long serialVersionUID = 7848805671649593049L;

    private Integer groupId;

    private Integer studentId;

    private String name;

    private byte[] data;

    private Collection<String> words;

    private static final Group FINAL_GROUP = new Group();

    private static final Student FINAL_STUDENT = new Student();

    private Group group = FINAL_GROUP;

    private Student student = FINAL_STUDENT;

    public ProcessImportVo(Integer groupId, Integer studentId, String name, byte[] data) {
        this.groupId = groupId;
        this.studentId = studentId;
        this.name = name;
        this.data = data;
    }
}

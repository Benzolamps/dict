package com.benzolamps.dict.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * Word文档方案实体类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-11 20:36:23
 */
@Getter
@Setter
@ToString
public class DocSolution implements Serializable {

    private static final long serialVersionUID = 2832049188732640789L;

    /** id */
    private Integer id;

    /** 名称 */
    private String name;

    /** 路径 */
    private String path;

    /** 备注 */
    private String remark;

    /** 是否需要头部 */
    private Boolean needHeader;

    /** 导出对象 */
    public enum ExportFor {

        /** 单词 */
        WORD,

        /** 短语 */
        PHRASE
    }

    /** 导出对象 */
    private ExportFor exportFor;

    /** 是否需要乱序 */
    private Boolean needShuffle;

    /** 排序 */
    private Integer order;

    /** 属性 */
    private Map<String, Object> properties;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!DocSolution.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        return Objects.equals(getId(), ((DocSolution) obj).getId());
    }


    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode += getId() != null ? getId().hashCode() * 31 : 0;
        hashCode += getClass().hashCode() * 31;
        return hashCode;
    }
}

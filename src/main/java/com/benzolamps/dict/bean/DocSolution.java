package com.benzolamps.dict.bean;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import java.util.Map;

/**
 * Word文档方案实体类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-11 20:36:23
 */
@Getter
@Setter
public class DocSolution extends BaseBean {

    private static final long serialVersionUID = 2832049188732640789L;

    /** id */
    @Id
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

    /** 属性 */
    private Map<String, Object> properties;
}

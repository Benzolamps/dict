package com.benzolamps.dict.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Word文档方案方案集合
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-11 20:51:09
 */
@Getter
@Setter
public class DocSolutions implements Serializable {

    private static final long serialVersionUID = 1050848490302868965L;

    /** 集合 */
    private Set<DocSolution> solutions;

    /** 基础属性 */
    private Map<String, Object> baseProperties;
}

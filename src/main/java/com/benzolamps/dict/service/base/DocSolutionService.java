package com.benzolamps.dict.service.base;

import com.benzolamps.dict.bean.DocSolution;
import java.util.List;
import java.util.Map;

/**
 * Word文档方案Service接口
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-11 21:23:42
 */
public interface DocSolutionService {

    /**
     * 获取全部乱序方案
     * @return 乱序方案列表
     */
    List<DocSolution> findAll();

    /**
     * 根据id寻找乱序方案
     * @param id id
     * @return 乱序方案
     */
    DocSolution find(Integer id);

    /**
     * 获取基础属性
     * @return 基础属性
     */
    Map<String, Object> getBaseProperties();
}

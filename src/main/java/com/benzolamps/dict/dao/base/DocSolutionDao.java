package com.benzolamps.dict.dao.base;

import com.benzolamps.dict.bean.DocSolution;

import java.util.List;
import java.util.Map;

/**
 * Word文档方案Dao接口
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-11 20:51:47
 */
public interface DocSolutionDao {

    /** @return 获取全部Word文档方案方案 */
    List<DocSolution> findAll();

    /**
     * 根据id查找Word文档方案方案
     * @param id id
     * @return Word文档方案方案
     */
    DocSolution find(Integer id);

    /** 重新加载 */
    void reload();

    /** 保存 */
    void flush();

    /** 获取基础属性 */
    Map<String, Object> getBaseProperties();
}

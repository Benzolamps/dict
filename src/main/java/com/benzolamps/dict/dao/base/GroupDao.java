package com.benzolamps.dict.dao.base;

import com.benzolamps.dict.bean.Group;
import com.benzolamps.dict.bean.Library;

import java.util.Map;

/**
 * 单词短语分组Dao接口
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-21 22:06:10
 */
public interface GroupDao extends BaseDao<Group> {
    /**
     * 获取最大值信息
     * @param library 词库
     * @return 最大值信息
     */
    Map<String, Number> findMaxInfo(Library library);
}

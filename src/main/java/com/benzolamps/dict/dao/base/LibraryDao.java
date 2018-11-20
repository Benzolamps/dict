package com.benzolamps.dict.dao.base;

import com.benzolamps.dict.bean.Library;

/**
 * 词库Dao接口
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 22:15:45
 */
public interface LibraryDao extends BaseDao<Library> {

    /**
     * 重置索引
     */
    void normalizeIndex();
}

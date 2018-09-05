package com.benzolamps.dict.service.base;

import com.benzolamps.dict.bean.Library;

/**
 * 词库Service接口
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 22:25:03
 */
public interface LibraryService extends BaseService<Library> {

    /**
     * 获取当前词库
     * @return 词库
     */
    Library getCurrent();
}

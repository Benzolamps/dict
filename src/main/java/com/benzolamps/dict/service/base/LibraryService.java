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

    /**
     * 设置当前词库
     * @param library 词库
     */
    void setCurrent(Library library);

    /**
     * 获取词库总个数
     * @return 个数
     */
    Integer count();

    /**
     * 检测词库名称是否存在
     * @param name 名称
     * @return 检测结果
     */
    boolean nameExists(String name);
}

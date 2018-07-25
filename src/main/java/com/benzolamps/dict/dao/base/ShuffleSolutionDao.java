package com.benzolamps.dict.dao.base;

import com.benzolamps.dict.bean.ShuffleSolution;
import com.benzolamps.dict.dao.core.Page;

import java.util.Collection;

/**
 * 乱序方案Dao接口
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-20 23:34:02
 */
public interface ShuffleSolutionDao {

    /** @return 获取全部乱序方案 */
    Page<ShuffleSolution> findAll();

    /**
     * 根据id寻找乱序方案
     * @param id id
     * @return 乱序方案
     */
    ShuffleSolution find(Long id);

    /**
     * 保存一个乱序方案
     * @param shuffleSolution 乱序方案
     * @return 乱序方案
     */
    ShuffleSolution persist(ShuffleSolution shuffleSolution);

    /**
     * 修改一个乱序方案
     * @param shuffleSolution 乱序方案
     * @return 乱序方案
     */
    ShuffleSolution update(ShuffleSolution shuffleSolution);

    /**
     * 删除一个乱序方案
     * @param shuffleSolution 乱序方案
     */
    void remove(ShuffleSolution shuffleSolution);

    /** 重新加载 */
    void reload();

    /** 保存 */
    void flush();

}

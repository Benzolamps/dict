package com.benzolamps.dict.dao.base;

import com.benzolamps.dict.bean.ShuffleSolution;
import com.benzolamps.dict.dao.core.Page;
import com.benzolamps.dict.dao.core.Pageable;

import java.util.List;

/**
 * 乱序方案Dao接口
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-20 23:34:02
 */
public interface ShuffleSolutionDao {

    /** @return 获取全部乱序方案 */
    List<ShuffleSolution> findAll();


    /** @return 获取全部乱序方案 */
    Page<ShuffleSolution> findPage(Pageable pageable);

    /**
     * 根据id查找乱序方案
     * @param id id
     * @return 乱序方案
     */
    ShuffleSolution find(Integer id);

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
     * @param shuffleSolutionId 乱序方案id
     */
    void remove(final Integer shuffleSolutionId);

    /** 重新加载 */
    void reload();

    /** 保存 */
    void flush();
}

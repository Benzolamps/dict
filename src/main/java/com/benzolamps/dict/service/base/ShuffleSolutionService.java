package com.benzolamps.dict.service.base;

import com.benzolamps.dict.component.IShuffleStrategySetup;
import com.benzolamps.dict.bean.ShuffleSolution;
import com.benzolamps.dict.dao.core.Page;

import java.util.Collection;
import java.util.Set;

/**
 * 乱序策略Service接口
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-19 2018-7-20 19:33:54
 */
public interface ShuffleSolutionService {

    /** @return 获取可用的乱序策略名 */
    Set<String> getAvailableCopyStrategyNames();

    /** @return 获取默认的乱序策略实例 */
    IShuffleStrategySetup getDefaultSolutionInstance();

    /** @return 根据id获取一个乱序策略实例 */
    IShuffleStrategySetup getSolutionInstanceAt(Long id);

    /** @return 获取所有的乱序方案 */
    Page<ShuffleSolution> getSolutions();

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
}

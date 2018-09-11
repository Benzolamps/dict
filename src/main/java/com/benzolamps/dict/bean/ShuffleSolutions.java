package com.benzolamps.dict.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 乱序方案集合
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-20 23:30:26
 */
@Data
public class ShuffleSolutions implements Serializable {

    private static final long serialVersionUID = -5570719228526398391L;

    /** 集合 */
    private Set<ShuffleSolution> solutions;
}

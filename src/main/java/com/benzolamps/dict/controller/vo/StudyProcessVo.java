package com.benzolamps.dict.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 学习进度Vo
 * @author Benzolamps
 * @version 2.1.5
 * @datetime 2018-10-2 12:59:59
 */
@Data
@AllArgsConstructor
public class StudyProcessVo implements Serializable {

    private static final long serialVersionUID = 2232499968500674516L;

    /** 已掌握的个数 */
    private Number masteredCount;

    /** 未掌握的个数 */
    private Number failedCount;

    /** 未学习的个数 */
    private Number unstudiedCount;

    /** 总数 */
    private Number wholeCount;
}

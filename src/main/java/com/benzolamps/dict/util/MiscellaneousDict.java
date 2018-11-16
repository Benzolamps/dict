package com.benzolamps.dict.util;

import com.benzolamps.dict.dao.core.Filter;
import org.springframework.util.Assert;

/**
 * 杂项工具类
 * @author Benzolamps
 * @version 2.3.1
 * @datetime 2018-11-16 14:29:17
 */
public interface MiscellaneousDict {

    static Filter switch10(String field, int value, boolean gt0, boolean gt1, boolean gt10) {
        Assert.hasText(field, "field不能为null或空");
        int option = 10;
        if (value <= 10) {
            return Filter.eq(field, value);
        } else if (gt0 && value == ++option) {
            return Filter.gt(field, 0);
        } else if (gt1 && value == ++option) {
            return Filter.gt(field, 1);
        } else if (gt10 && value == ++option) {
            return Filter.gt(field, 10);
        } else {
            return null;
        }
    }

    static Filter switch100(String field, int value, boolean gt0, boolean gt1, boolean gt100, boolean eq1) {
        Assert.hasText(field, "field不能为null或空");
        int option = 10;
        if (value == 0) {
            return Filter.eq(field, 0);
        } else if (value <= 10) {
            return Filter.betweenAnd(field, (value - 1) * 10 + 1, value * 10);
        } else if (gt0 && value == ++option) {
            return Filter.gt(field, 0);
        } else if (gt1 && value == ++option) {
            return Filter.gt(field, 1);
        } else if (gt100 && value == ++option) {
            return Filter.gt(field, 100);
        } else if (eq1 && value == ++option) {
            return Filter.eq(field, 1);
        } else {
            return null;
        }
    }

    static Filter switch1000(String field, int value, boolean gt0, boolean gt1, boolean gt1000, boolean eq1) {
        Assert.hasText(field, "field不能为null或空");
        int option = 13;
        if (value == 0) {
            return Filter.eq(field, 0);
        } else if (value <= 10) {
            return Filter.betweenAnd(field, (value - 1) * 10 + 1, value * 10);
        } else if (value == 11) {
            return Filter.betweenAnd(field, 101, 200);
        } else if (value == 12) {
            return Filter.betweenAnd(field, 201, 500);
        } else if (value == 13) {
            return Filter.betweenAnd(field, 501, 1000);
        } else if (gt0 && value == ++option) {
            return Filter.gt(field, 0);
        } else if (gt1 && value == ++option) {
            return Filter.gt(field, 1);
        } else if (gt1000 && value == ++option) {
            return Filter.gt(field, 1000);
        } else if (eq1 && value == ++option) {
            return Filter.eq(field, 1);
        } else {
            return null;
        }
    }
}

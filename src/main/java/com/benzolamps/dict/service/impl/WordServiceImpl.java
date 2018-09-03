package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.Word;
import com.benzolamps.dict.dao.core.Order;
import com.benzolamps.dict.dao.core.Page;
import com.benzolamps.dict.dao.core.Pageable;
import com.benzolamps.dict.service.base.WordService;
import org.springframework.stereotype.Service;

/**
 * 单词Service接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 22:22:32
 */
@Service
public class WordServiceImpl extends BaseServiceImpl<Word> implements WordService {

    @Override
    public Page<Word> findPage(Pageable pageable) {
        pageable.getOrders().add(Order.asc("index"));
        return super.findPage(pageable);
    }
}

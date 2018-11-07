package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.Clazz;
import com.benzolamps.dict.service.base.ClazzService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 班级Service接口实现类
 * @author Benzolamps
 * @version 2.1.4
 * @datetime 2018-9-19 22:01:23
 */
@Service("clazzService")
@Transactional
public class ClazzServiceImpl extends BaseServiceImpl<Clazz> implements ClazzService {
}

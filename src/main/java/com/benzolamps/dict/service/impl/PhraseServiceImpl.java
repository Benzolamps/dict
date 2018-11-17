package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.Phrase;
import com.benzolamps.dict.controller.vo.PhraseExcelVo;
import com.benzolamps.dict.service.base.PhraseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.OutputStream;
import java.util.List;

/**
 * 短语Service接口实现类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 22:28:07
 */
@Service("phraseService")
@Transactional
public class PhraseServiceImpl extends BaseElementServiceImpl<Phrase, PhraseExcelVo> implements PhraseService {
    @Override
    public void toExcel(List<Phrase> phrases, OutputStream outputStream) {
        throw new UnsupportedOperationException("该功能尚未实现！");
    }
}

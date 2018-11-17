package com.benzolamps.dict.service.base;

import com.benzolamps.dict.bean.Phrase;

import java.io.OutputStream;
import java.util.List;

/**
 * 短语Service接口
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 22:27:06
 */
public interface PhraseService extends BaseElementService<Phrase> {

    /**
     * 导出excel
     * @param phrases 短语集合
     * @param outputStream 输出流
     */
    @SuppressWarnings("unused")
    void toExcel(List<Phrase> phrases, OutputStream outputStream);
}

package com.benzolamps.dict.service.base;

import com.benzolamps.dict.bean.Word;

import java.io.OutputStream;
import java.util.List;

/**
 * 单词Service接口
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-7-1 22:21:02
 */
public interface WordService extends BaseElementService<Word> {

    /**
     * 导出excel
     * @param words 单词集合
     * @param outputStream 输出流
     */
    void toExcel(List<Word> words, OutputStream outputStream);
}

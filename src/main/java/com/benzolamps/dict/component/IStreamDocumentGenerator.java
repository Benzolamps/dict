package com.benzolamps.dict.component;

import com.benzolamps.dict.bean.BaseElement;

import java.io.OutputStream;
import java.util.Collection;

/**
 * IO流文档生成器
 * @author Benzolamps
 * @version 2.3.5
 * @datetime 2018-11-28 15:37:27
 */
public interface IStreamDocumentGenerator<T extends BaseElement> {

    /**
     * 获取扩展名
     * @return 扩展名
     */
    String getExt();

    /**
     * 生成
     * @param outputStream 输出流
     * @param elements 元素
     */
    void generate(OutputStream outputStream, Collection<T> elements, String title);
}

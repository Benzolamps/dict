package com.benzolamps.dict.util;

import com.benzolamps.dict.util.lambda.Action2;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 命令行工具类
 * @author Benzolamps
 * @version 2.3.3
 * @datetime 2018-11-18 11:13:22
 */
@UtilityClass
@Slf4j
public class DictCommand {
    /**
     * 执行命令行命令
     * @param cmd 命令
     * @param action 输入流与错误流回调
     */
    @SuppressWarnings("SpellCheckingInspection")
    @SneakyThrows(IOException.class)
    public static void exec(String cmd, Action2<String, String> action) {
        logger.info("cmd: " + cmd);
        Runtime runtime = Runtime.getRuntime();
        Process addProcess = runtime.exec(cmd);
        Charset gbk = Charset.forName("GBK");
        try (InputStream is = addProcess.getInputStream(); InputStream es = addProcess.getErrorStream()) {
            String istr = StreamUtils.copyToString(is, gbk);
            String estr = StreamUtils.copyToString(es, gbk);
            action.accept(istr, estr);
        }
    }
}
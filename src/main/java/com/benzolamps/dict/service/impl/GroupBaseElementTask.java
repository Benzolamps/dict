package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.cfg.AipProperties;
import com.benzolamps.dict.controller.vo.ProcessImportVo;
import com.benzolamps.dict.exception.ProcessImportException;
import com.benzolamps.dict.util.DictSpring;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 单词导入任务
 * @author Benzolamps
 * @version 2.2.9
 * @datetime 2018-11-10 21:51:53
 */
@RequiredArgsConstructor
public class GroupBaseElementTask implements Runnable {

    private final ProcessImportVo processImportVo;

    private AipProperties aipProperties = DictSpring.getBean(AipProperties.class);

    @Override
    public void run() {
        getBaseElements(processImportVo.getData());
    }

    @SuppressWarnings("ConstantConditions")
    private List<String> getResult(Supplier<JSONObject> supplier) {
        JSONObject res = new JSONObject();
        /* 18代表达到每秒调用上限 */
        Object errorCode = 18;
        while (errorCode != null) {
            Assert.isTrue(errorCode.equals(18), errorCode + "：" + res.opt("error_msg"));
            res = supplier.get();
            errorCode = res.opt("error_code");
        }
        String regex = "[A-Za-z]+";
        List<String> words = new ArrayList<>();
        for (int i = 0; i < res.getJSONArray("words_result").length(); i++) {
            String word = (String) res.getJSONArray("words_result").getJSONObject(i).get("words");
            Matcher matcher = Pattern.compile(regex).matcher(word);
            while (matcher.find()) {
                words.add(matcher.group().toLowerCase());
            }
        }
        return words;
    }

    @SuppressWarnings({"unchecked", "StatementWithEmptyBody", "LoopConditionNotUpdatedInsideLoop"})
    private void getBaseElements(byte[] data) {
        try {
            /* Vector是线程安全的 */
            List<String> words = new Vector<>();
            /* 开启低精度导入线程 */
            Thread thread = new Thread(() -> words.addAll(getResult(() -> aipProperties.basicGeneral(data, new HashMap<>()))));
            thread.start();
            words.addAll(getResult(() -> aipProperties.basicAccurateGeneral(data, new HashMap<>())));
            /* 检测低精度线程是否结束 */
            while (thread.isAlive());
            processImportVo.setWords(words);
        } catch (Throwable e) {
            throw new ProcessImportException(processImportVo.getName(), null, e);
        }
    }
}

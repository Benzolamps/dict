package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.bean.BaseElement;
import com.benzolamps.dict.cfg.AipProperties;
import com.benzolamps.dict.controller.vo.ProcessImportVo;
import com.benzolamps.dict.exception.DictException;
import com.benzolamps.dict.exception.ProcessImportException;
import com.benzolamps.dict.util.DictSpring;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
public class GroupBaseElementThread<T extends BaseElement> extends Thread {

    private final ProcessImportVo processImportVo;

    private AipProperties aipProperties = DictSpring.getBean(AipProperties.class);

    @Override
    public void run() {
        getBaseElements(processImportVo.getData());
    }

    @SuppressWarnings({"unchecked", "StatementWithEmptyBody", "LoopConditionNotUpdatedInsideLoop"})
    private void getBaseElements(byte[] data) {
        try {
            List<String> words = new ArrayList<>();
            String regex = "[A-Za-z]+";
            Thread thread = new Thread(() -> {
                JSONObject res;
                Object errorCode;
                do {
                    res = aipProperties.basicGeneral(data);
                    errorCode = res.opt("error_code");
                } while (errorCode != null && errorCode.equals(18));
                if (errorCode != null && !errorCode.equals(18)) {
                    throw new DictException(errorCode + "：" + res.get("error_msg"));
                }
                for (int i = 0; i < res.getJSONArray("words_result").length(); i++) {
                    String word = (String) res.getJSONArray("words_result").getJSONObject(i).get("words");
                    Matcher matcher = Pattern.compile(regex).matcher(word);
                    while (matcher.find()) {
                        word = matcher.group().toLowerCase();
                        words.add(word);
                    }
                }
            });
            thread.start();
            JSONObject res;
            Object errorCode;
            do {
                res = aipProperties.basicAccurateGeneral(data);
                errorCode = res.opt("error_code");
            } while (errorCode != null && errorCode.equals(18));
            if (errorCode != null && !errorCode.equals(18)) {
                throw new DictException(errorCode + "：" + res.get("error_msg"));
            }
            for (int i = 0; i < res.getJSONArray("words_result").length(); i++) {
                String word = (String) res.getJSONArray("words_result").getJSONObject(i).get("words");
                Matcher matcher = Pattern.compile(regex).matcher(word);
                while (matcher.find()) {
                    word = matcher.group().toLowerCase();
                    words.add(word);
                }
            }
            while (thread.isAlive());
            processImportVo.setWords(words);
        } catch (Throwable e) {
            throw new ProcessImportException(processImportVo.getName(), null, e);
        }
    }
}

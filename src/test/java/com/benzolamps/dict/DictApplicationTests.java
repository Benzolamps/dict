package com.benzolamps.dict;

import com.benzolamps.dict.main.DictApplication;
import org.junit.Test;

import java.util.function.IntPredicate;

public class DictApplicationTests {
    @Test
    public void contextLoads() {
        DictApplication.main("--spring.profiles.active=release");
    }

    @SuppressWarnings({"NonAsciiCharacters", "DanglingJavadoc", "ShiftOutOfRange", "InfiniteLoopStatement"})
    @Test
    public void joke() {

        int ¥$¥ = -355555550;
        IntPredicate p = ¥¥¥$$$¥¥¥->¥$¥>>¥$¥-/**💰💰💰💰💰💰💰💰💰💰**/-¥$¥<<¥$¥<-¥¥¥$$$¥¥¥;

        System.out.println(¥$¥>>¥$¥-/**💰💰💰💰💰💰💰💰💰💰**/-¥$¥<<¥$¥);
        if (p.test(666666)) {
            System.out.println("不给钱就捣乱\ud83d\udcb0\ud83d\udcb0\ud83d\udcb0\ud83d\udcb0\ud83d\udcb0\ud83d\udcb0");
        }
    }
}

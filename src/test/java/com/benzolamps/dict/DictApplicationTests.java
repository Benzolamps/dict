package com.benzolamps.dict;

import com.benzolamps.dict.main.DictApplication;
import org.junit.Test;

public class DictApplicationTests {
    @Test
    public void contextLoads() {
        DictApplication.main("--spring.profiles.active=release");
    }
}

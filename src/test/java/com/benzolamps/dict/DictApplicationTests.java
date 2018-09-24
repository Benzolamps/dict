package com.benzolamps.dict;

import com.benzolamps.dict.bean.Group.Status;
import com.benzolamps.dict.main.DictApplication;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class DictApplicationTests {
    // @Test
    public void contextLoads() {
        DictApplication.main("--spring.profiles.active=release");
    }
    
    @Test
    public void testEnumJsonValue() throws IOException {
        Status status = Status.COMPLETED;
        
        ObjectMapper objectMapper = new ObjectMapper();
        
        List<Status> statuses = Collections.singletonList(status);
        
        System.out.println(statuses);
        
        System.out.println(objectMapper.writeValueAsString(statuses));
        
        
    }
}

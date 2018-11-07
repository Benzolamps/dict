package com.benzolamps.dict;

import com.benzolamps.dict.util.DictQrCode;
import com.google.zxing.NotFoundException;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;

public class qrTest {

    @Test
    public void test() throws IOException {
//        byte[] qr = DictQrCode.createQrCode("{\"student\": 1, \"group\": 4}", 100, 100);
    //    System.out.println(Base64.getEncoder().encodeToString(qr));
        try {
            byte[] bytes = StreamUtils.copyToByteArray(new FileSystemResource("E:\\test1573.jpg").getInputStream());
            System.out.println(DictQrCode.readQrCode(bytes));
        } catch (Exception e) {
            System.out.println(333);
            e.printStackTrace();
        }

//        byte[] bytes = Base64.getDecoder().decode("QmI8kOs=");
//        Random random = new Random(2018);
//        for (int i = 0; i < bytes.length; i++) {
//            bytes[i] ^= (random.nextInt(Byte.MAX_VALUE * 2) - Byte.MAX_VALUE);
//        }
//        String value = new String(bytes, "UTF-8");
//        System.out.println(value);
    }
}

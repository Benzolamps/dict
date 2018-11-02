package com.benzolamps.dict.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.SneakyThrows;
import org.springframework.util.Assert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码工具类
 * @author 国文源
 * @version 2.2.3
 * @datetime 2018-11-2 15:10:36
 */
public interface DictQrCode {
    /**
     * 创建二维码
     * @param content 内容
     * @param width 宽度
     * @param height 高度
     * @return byte[]
     */
    @SneakyThrows({IOException.class, WriterException.class})
    static byte[] createQrCode(String content, int width, int height) {
        Assert.hasText(content, "content不能为null或空");
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 2);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(bitMatrix, "png", outputStream);
            MatrixToImageWriter.writeToPath(bitMatrix, "png", new File("E:\\test.png").toPath());
            return outputStream.toByteArray();
        }
    }

    @SneakyThrows({IOException.class, NotFoundException.class})
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    static String readQrCode(byte[] qr) {
        Result result = null;
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(qr));
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
            HashMap hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            result = new MultiFormatReader().decode(bitmap, hints);
        return result.getText();
    }
}

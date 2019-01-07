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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.benzolamps.dict.util.Constant.UTF8_CHARSET;

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
        hints.put(EncodeHintType.CHARACTER_SET, UTF8_CHARSET);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 2);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(bitMatrix, "png", outputStream);
            return outputStream.toByteArray();
        }
    }

    /**
     * 识别二维码
     * @param qr 二维码
     * @return 内容
     */
    @SneakyThrows(IOException.class)
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    static String readQrCode(byte[] qr, Float x, Float width, Float y, Float height) throws NotFoundException {
        Result result;
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(qr));
        x = Optional.ofNullable(x).orElse(0F);
        width = Optional.ofNullable(width).orElse(1F);
        y = Optional.ofNullable(y).orElse(0F);
        height = Optional.ofNullable(height).orElse(1F);
        bufferedImage = bufferedImage.getSubimage(
            (int) (x * bufferedImage.getWidth()),
            (int) (y * bufferedImage.getHeight()),
            (int) (width * bufferedImage.getWidth()),
            (int) (height * bufferedImage.getHeight())
        );
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
        HashMap hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, UTF8_CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        return result.getText();
    }
}

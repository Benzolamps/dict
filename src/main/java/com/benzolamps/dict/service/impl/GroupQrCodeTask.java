package com.benzolamps.dict.service.impl;

import com.benzolamps.dict.controller.vo.ProcessImportVo;
import com.benzolamps.dict.exception.ProcessImportException;
import com.benzolamps.dict.util.DictQrCode;
import com.google.zxing.NotFoundException;
import lombok.AllArgsConstructor;

import java.util.Base64;
import java.util.Random;

@AllArgsConstructor
public class GroupQrCodeTask implements Runnable {

    private final ProcessImportVo processImportVo;

    @Override
    public final void run() {
        if (processImportVo.getStudentId() == null || processImportVo.getGroupId() == null) {
            try {
                String content = DictQrCode.readQrCode(processImportVo.getData(), 0.75F, 0.25F, 0F, 0.15F);
                byte[] bytes = Base64.getDecoder().decode(content);
                Random random = new Random(2018);
                for (int i = 0; i < bytes.length; i++) {
                    bytes[i] ^= (random.nextInt(Byte.MAX_VALUE * 2) - Byte.MAX_VALUE);
                }
                String[] value = new String(bytes, "UTF-8").split(",");
                Integer studentId = Integer.valueOf(value[0]);
                Integer groupId = Integer.valueOf(value[1]);
                if (processImportVo.getStudentId() == null) {
                    processImportVo.setStudentId(studentId);
                }
                if (processImportVo.getGroupId() == null) {
                    processImportVo.setGroupId(groupId);
                }
            } catch (NotFoundException e) {
                if (processImportVo.getStudentId() == null) {
                    throw new ProcessImportException(processImportVo.getName(), "未识别到二维码", e);
                }
            } catch (Throwable e) {
                throw new ProcessImportException(processImportVo.getName(), null, e);
            }
        }
    }
}

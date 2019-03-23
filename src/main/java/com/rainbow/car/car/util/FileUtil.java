package com.rainbow.car.car.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * @author caijiatao
 * @date 2019/3/12 4:36 PM
 */
public class FileUtil {
    /**
     * @param root     根目录
     * @param file
     * @param dirName
     * @param fileName
     * @return
     */
    public static String saveFile(String root, MultipartFile file, String dirName, String fileName) throws IOException {
        String dir = root + File.separator + dirName;
        File desDir = new File(dir);

        // 文件后缀名
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        File newFile = new File(desDir, fileName + DateUtil.getFileFormatDate(new Date()) + suffix);

        if (desDir.exists()) {
            file.transferTo(newFile);
        } else {
            desDir.mkdirs();
            file.transferTo(newFile);
        }
        return newFile.getAbsolutePath();
    }
}

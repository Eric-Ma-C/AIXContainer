package org.zju.vipa.aix.container.client.utils;

import java.io.File;
import java.io.FileInputStream;

import java.io.FileWriter;
import java.io.IOException;

/**
 * @Date: 2020/2/14 11:14
 * @Author: EricMa
 * @Description: 文件操作
 */
public class FileUtils {
    /**
     * 获取文件内容
     *
     * @param filePath 文件
     * @return 内容
     */
    public static String getContent(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
//            LogUtils.info("File is not exists.");
            return null;
        }

        try
            (FileInputStream fis = new FileInputStream(file)) {
            byte[] b = new byte[(int) file.length()];
            fis.read(b);

            return new String(b, "UTF-8");
        } catch (IOException e) {
            ClientExceptionUtils.handle(e);
        }

        return null;
    }

    /**
     * 将内容写入文件中
     *
     * @param content 写入内容
     * @param path    文件路径（如：/a/b/test.txt）
     */
    public static void write(String content, String path) {
        // 检测文件夹是否存在，不存在则创建文件夹和文件
        createFile(path);
        FileWriter writer = null;
        try {
            writer = new FileWriter(path);
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            ClientExceptionUtils.handle(e);
        }
    }

    /**
     * 创建文件
     *
     * @param path 文件路径
     * @return 文件路径（如：F:/a/b/test.txt）
     */
    private static File createFile(String path) {
        // 创建文件夹
        if (path.contains("/")) {
            String[] split = path.split("/");
            String fileName = split[split.length - 1];
            String dirPath = path.replace(fileName, "");
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                ClientExceptionUtils.handle(e);
            }
        }
        return file;
    }
}

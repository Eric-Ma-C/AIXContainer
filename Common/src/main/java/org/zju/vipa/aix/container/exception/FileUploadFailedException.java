package org.zju.vipa.aix.container.exception;

/**
 * @Date: 2020/5/27 9:16
 * @Author: EricMa
 * @Description: 文件上传失败异常
 */
public class FileUploadFailedException extends BaseException{

    public FileUploadFailedException(String msg) {
        super(msg);
    }
}

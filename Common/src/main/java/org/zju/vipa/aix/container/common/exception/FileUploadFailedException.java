package org.zju.vipa.aix.container.common.exception;

/**
 * @Date: 2020/5/27 9:16
 * @Author: EricMa
 * @Description: todo 自定义异常 继承分化  文件上传失败异常
 */
public class FileUploadFailedException extends AIXBaseException {

    public FileUploadFailedException(String msg) {
        super(msg);
    }
}

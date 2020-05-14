package org.zju.vipa.aix.container.config;

/**
 * @Date: 2020/3/30 14:13
 * @Author: EricMa
 * @Description: 全局配置
 */
public class Config {
    /** 容器aix根目录 */
    public static final String AIX_ROOT_DIR="/home/aix";
    /** 容器中token文件位置 */
    public static final String TOKEN_FILE=AIX_ROOT_DIR+"/token";


    /** 容器中model.zip文件下载位置 */
    public static final String MODEL_SAVE_PATH =AIX_ROOT_DIR+"/model.zip";
    /** 容器中model.zip文件解压位置 */
    public static final String MODEL_UNZIP_PATH =AIX_ROOT_DIR+"/model";

    /** 容器中dataset文件下载位置 */
    public static final String DATASET_SAVE_PATH =AIX_ROOT_DIR+"/annotations.json";
//    /** 容器中model.zip文件解压位置 */
//    public static final String DATASET_UNZIP_PATH =AIX_ROOT_DIR+"/model";




    public static final String NULL_TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJudWxsIn0.rgzK-LqCRYGoUfrbLvqv0lqveUD_0raqOV-2TXjq_ig";
}

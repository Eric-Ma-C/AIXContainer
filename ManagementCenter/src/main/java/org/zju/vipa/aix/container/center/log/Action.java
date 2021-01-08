package org.zju.vipa.aix.container.center.log;

/**
 * @Date: 2021/1/8 10:56
 * @Author: EricMa
 * @Description: 日志行为,方便logstash筛选
 */
public enum Action {
    /**
     * 容器注册
     */
    CLIENT_REGISTER;


    @Override
    public String toString() {
        return "[ACTION:"+this.name()+"] ";
    }
}

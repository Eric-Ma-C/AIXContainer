package zju.vipa.container.client.shell;

/**
 * @Date: 2020/1/11 21:47
 * @Author: EricMa
 * @Description: shell指令执行回调接口
 */
interface RealtimeProcessInterface{
    /** shell开始执行 */
    void onProcessBegin(String cmd);
    /** shell标准输出响应 */
    void onNewStdoutListener(String newStdout);
    /** shell错误输出响应 */
    void onNewStderrListener(String newStderr);
    /** shell结束输出响应 */
    void onProcessFinish(int resultCode);
    //void execCommand(String ...commands);
}
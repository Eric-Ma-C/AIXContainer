package zju.vipa.aix.container.client.shell;

/**
 * @Date: 2020/1/11 21:47
 * @Author: EricMa
 * @Description: shell指令执行回调接口
 */
interface RealtimeProcessListener {
    /** shell开始执行 */
    void onProcessBegin(String cmd);
    /** shell标准输出响应 */
    void onNewStdOut(String newStdout);
    /** shell错误输出响应 */
    void onNewStdError(String newStderr);
    /** shell结束输出响应 */
    void onProcessFinished(int resultCode);
    //void execCommand(String ...commands);
}
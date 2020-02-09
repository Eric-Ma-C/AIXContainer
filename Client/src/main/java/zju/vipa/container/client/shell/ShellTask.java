package zju.vipa.container.client.shell;

import zju.vipa.container.utils.ExceptionUtils;
import zju.vipa.container.client.network.TcpClient;
import zju.vipa.container.message.Intent;
import zju.vipa.container.utils.LogUtils;

/**
 * @Date: 2020/1/11 21:49
 * @Author: EricMa
 * @Description: 非阻塞 execute shell command
 */
public class ShellTask implements RealtimeProcessInterface {

    private RealtimeProcess mRealtimeProcess = null;
    private String[] command;
    private String cmdDir;

    public ShellTask(String cmd,String cmdDir) {
        this.command = cmd.split("\\s+");
        this.cmdDir=cmdDir;
    }

    public ShellTask(String cmd) {
        this.command = cmd.split("\\s+");
        this.cmdDir="/";
    }

    public void exec() {
        mRealtimeProcess = new RealtimeProcess(this);
        mRealtimeProcess.setDirectory(cmdDir);
        mRealtimeProcess.setCommand(command);

        try {
            mRealtimeProcess.start();
        } catch (Exception e) {
            ExceptionUtils.handle(e);
        }

        //System.out.println(mRealtimeProcess.getAllResult());

    }

    /** shell指令执行回调接口实现 todo:回传center*/
    @Override
    public void onNewStdoutListener(String newStdOut) {
        if ("".equals(newStdOut)||newStdOut==null) {
            return;
        }
        LogUtils.info(newStdOut);
        TcpClient.getInstance().uploadShellState(Intent.shellInfo,newStdOut);
    }

    @Override
    public void onNewStderrListener(String newStdErr) {
        if ("".equals(newStdErr)||newStdErr==null) {
            return;
        }
        LogUtils.error("Shell Error :"+newStdErr);
        TcpClient.getInstance().uploadShellState(Intent.shellError,newStdErr);

    }

    @Override
    public void onProcessFinish(int resultCode) {
        LogUtils.debug("Shell Finished :"+resultCode);
        TcpClient.getInstance().uploadShellState(Intent.shellResult,"resultCode="+resultCode);

    }
}

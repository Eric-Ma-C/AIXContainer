package zju.vipa.aix.container.client.shell;

import zju.vipa.aix.container.client.network.ClientMessage;
import zju.vipa.aix.container.client.network.TcpClient;
import zju.vipa.aix.container.client.utils.ClientExceptionUtils;
import zju.vipa.aix.container.client.utils.ClientLogUtils;
import zju.vipa.aix.container.message.Intent;

import java.io.IOException;

/**
 * @Date: 2020/1/11 21:49
 * @Author: EricMa
 * @Description: 非阻塞 execute shell command
 */
public class ShellTask implements RealtimeProcessInterface {

    private RealtimeProcess mRealtimeProcess = null;
//    private String command;
//    private String cmdDir;

    public ShellTask(String cmd, String cmdDir) {
        mRealtimeProcess = new RealtimeProcess(this);
        mRealtimeProcess.setExecDir(cmdDir);
        mRealtimeProcess.setCommand(cmd);
    }

    public ShellTask(String cmd) {
        mRealtimeProcess = new RealtimeProcess(this);
        mRealtimeProcess.setExecDir(System.getProperty("user.home"));
        mRealtimeProcess.setCommand(cmd);
    }

    public ShellTask(String[] cmds) {
        mRealtimeProcess = new RealtimeProcess(this);
        mRealtimeProcess.setExecDir(System.getProperty("user.home"));
        String cmdList="";
        for (String cmd : cmds) {
            cmdList=cmdList.concat(cmd+" && ");
        }
        mRealtimeProcess.setCommand(cmdList.substring(0,cmdList.length()-4));
    }

    public void exec() {

        try {
            mRealtimeProcess.start();
        } catch (IOException e) {
            ClientExceptionUtils.handle(e);
        }

        //System.out.println(mRealtimeProcess.getAllResult());

    }

    @Override
    public void onProcessBegin(String cmd) {
        ClientLogUtils.info("ShellTask: " + cmd + "  execDir: " + mRealtimeProcess.getExecDir());
        TcpClient.getInstance().sendMessage(new ClientMessage(Intent.SHELL_BEGIN, cmd));
    }

    /**
     * shell指令执行回调接口实现
     */
    @Override
    public void onNewStdoutListener(String newStdOut) {
        if ("".equals(newStdOut) || newStdOut == null) {
            return;
        }
        ClientLogUtils.info(newStdOut);
        TcpClient.getInstance().sendMessage(new ClientMessage(Intent.SHELL_INFO,newStdOut));
    }

    @Override
    public void onNewStderrListener(String newStdErr) {
        if ("".equals(newStdErr) || newStdErr == null) {
            return;
        }
        ClientLogUtils.error("Shell Error :" + newStdErr);
        TcpClient.getInstance().sendMessage(new ClientMessage(Intent.SHELL_ERROR, newStdErr));

    }

    @Override
    public void onProcessFinish(int resultCode) {
        ClientLogUtils.debug("Shell Finished :" + resultCode);
        TcpClient.getInstance().reportShellResult(new ClientMessage(Intent.SHELL_RESULT, "resultCode=" + resultCode));

    }
}

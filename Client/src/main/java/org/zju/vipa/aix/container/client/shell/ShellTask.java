package org.zju.vipa.aix.container.client.shell;

import org.zju.vipa.aix.container.client.env.ClientErrorParser;
import org.zju.vipa.aix.container.client.network.ClientMessage;
import org.zju.vipa.aix.container.client.network.TcpClient;
import org.zju.vipa.aix.container.client.utils.ClientExceptionUtils;
import org.zju.vipa.aix.container.client.utils.ClientLogUtils;
import org.zju.vipa.aix.container.common.message.Intent;

import java.io.IOException;

/**
 * @Date: 2020/1/11 21:49
 * @Author: EricMa
 * @Description: 非阻塞 execute shell command
 */
public class ShellTask implements RealtimeProcessListener {

    private RealtimeProcess mRealtimeProcess = null;
    private HandleShellErrorListener handleShellErrorListener;
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
        String cmdList = "";
        for (String cmd : cmds) {
            cmdList = cmdList.concat(cmd + " && ");
        }
        mRealtimeProcess.setCommand(cmdList.substring(0, cmdList.length() - 4));
    }

    public void exec(HandleShellErrorListener listener) {
        this.handleShellErrorListener = listener;
        exec();

        //System.out.println(mRealtimeProcess.getAllResult());

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
        ClientLogUtils.info(cmd);
//        if (Client.isUploadRealtimeLog) {
//            TcpClient.getInstance().sendMessage(new ClientMessage(Intent.SHELL_BEGIN, cmd));
//        }
    }

    /**
     * shell指令执行回调接口实现
     */
    @Override
    public void onNewStdOut(String newStdOut) {
        if ("".equals(newStdOut) || newStdOut == null) {
            return;
        }
        ClientLogUtils.info(newStdOut);
//        if (Client.isUploadRealtimeLog) {
//            TcpClient.getInstance().sendMessage(new ClientMessage(Intent.SHELL_INFO, newStdOut));
//        }
    }

    @Override
    public void onNewStdError(String newStdErr) {
        if ("".equals(newStdErr) || newStdErr == null) {
            return;
        }
        ClientLogUtils.error(newStdErr);

        String moduleName = ClientErrorParser.handleModuleNotFoundError(newStdErr);
        if (moduleName != null) {
            /** 平台不用尝试解析错误类型了 */
            if (handleShellErrorListener != null) {
                handleShellErrorListener.onHandle(moduleName);
            }
//            Intent intent = Intent.SHELL_ERROR;
//            if (Client.isUploadRealtimeLog) {
//                /** 只报告shell error，平台不做什么处理 */
//                TcpClient.getInstance().sendMessage(new ClientMessage(intent, newStdErr));
//            }
        } else {
            /** 无法解析的错误信息一直需要上传平台解析 */
            TcpClient.getInstance().sendMessage(new ClientMessage(Intent.SHELL_ERROR_HELP, newStdErr));

        }


    }

    @Override
    public void onProcessFinished(int resultCode) {

        ClientLogUtils.info("resultCode={}",resultCode);
//        if (Client.isUploadRealtimeLog) {
//            TcpClient.getInstance().sendMessage(new ClientMessage(Intent.SHELL_RESULT, "resultCode=" + resultCode));
//        }


//        TcpClient.getInstance().reportShellResult(new ClientMessage(Intent.SHELL_RESULT, "resultCode=" + resultCode));

    }

    public interface HandleShellErrorListener {
        /**
         * 设置修复命令
         */
        void onHandle(String moduleName);
    }
}

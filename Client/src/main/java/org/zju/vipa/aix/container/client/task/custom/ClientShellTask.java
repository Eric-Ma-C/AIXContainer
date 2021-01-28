package org.zju.vipa.aix.container.client.task.custom;

import org.zju.vipa.aix.container.client.shell.ServerResponseListener;
import org.zju.vipa.aix.container.client.shell.ShellProcess;
import org.zju.vipa.aix.container.client.task.BaseTask;

/**
 * @Date: 2020/3/17 22:24
 * @Author: EricMa
 * @Description: 自定义shell指令
 */
public class ClientShellTask extends BaseTask {

    public ClientShellTask(String cmds) {
        setCommands(new String[]{cmds});
    }
//    @Override
//    protected String[] initTaskCmds() {
//        return null;
//    }

    @Override
    protected void run(ServerResponseListener responseListener) {
//        ClientLogUtils.debug("ClientShellTask.procedure() begin");
        /** 没有及时退出 */
        shellProcess=new ShellProcess(getCommands());
        shellProcess.exec(shellErrorListener,responseListener);
    }

}

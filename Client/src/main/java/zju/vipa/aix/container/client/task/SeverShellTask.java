package zju.vipa.aix.container.client.task;

import zju.vipa.aix.container.client.shell.ShellTask;

/**
 * @Date: 2020/3/17 22:24
 * @Author: EricMa
 * @Description: 服务器下发的shell指令
 */
public class SeverShellTask extends BaseTask {
    String cmds;

    public SeverShellTask(String cmds) {
        this.cmds = cmds;
    }

    @Override
    String[] initTaskCmds() {
        return new String[]{cmds};
    }

    @Override
    void procedure() {
        new ShellTask(getCommands()).exec();
    }
}

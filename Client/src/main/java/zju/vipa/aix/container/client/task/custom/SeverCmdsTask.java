package zju.vipa.aix.container.client.task.custom;

import zju.vipa.aix.container.client.shell.ShellTask;
import zju.vipa.aix.container.client.task.BaseTask;
import zju.vipa.aix.container.client.utils.ClientLogUtils;

/**
 * @Date: 2020/3/17 22:24
 * @Author: EricMa
 * @Description: 服务器下发的shell指令
 */
public class SeverCmdsTask extends BaseTask {

    public SeverCmdsTask(String cmds) {
        setCommands(new String[]{cmds});
    }

    @Override
    protected String[] initTaskCmds() {
        return null;
    }

    @Override
    protected void procedure() {
        ClientLogUtils.debug("procedure() begin");
        /** 没有及时退出 */
        new ShellTask(getCommands()).exec();
    }
}
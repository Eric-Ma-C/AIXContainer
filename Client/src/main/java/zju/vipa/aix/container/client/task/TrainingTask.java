package zju.vipa.aix.container.client.task;

import zju.vipa.aix.container.client.shell.ShellTask;

/**
 * @Date: 2020/3/13 11:10
 * @Author: EricMa
 * @Description: aix平台中用户上传的训练任务
 */
public class TrainingTask extends BaseTask{
    @Override
    String[] initTaskCmds() {
        return new String[]{
            "cd /root/aix/code",
            "pwd",
            "source /root/miniconda3/bin/activate pip-env",
            "pip uninstall numpy -y",
            "pip install -U numpy==1.17.0",
            "PYTHONIOENCODING=utf-8 python -u -m  recognition.main -name prov_test"
        };
    }

    @Override
     void procedure() {
        new ShellTask(getCommands()).exec();
    }
}

import zju.vipa.aix.container.client.task.BaseTask;
import zju.vipa.aix.container.client.task.custom.ClientShellTask;
import zju.vipa.aix.container.client.thread.ClientThreadManager;

/**
 * @Date: 2020/1/11 19:49
 * @Author: EricMa
 * @Description: 测试client ,shell
 */
public class TestClient {

//    public static void main(String[] args) {
//        new FinallyTest().test(0);
//        System.out.println("------------------");
//        new FinallyTest().test(1);
//    }
//
//
//    static class FinallyTest {
//        public void test(int a) {
//            try {
//                int i = 0 / a;
//            } catch (Exception ex) {
//                System.err.println("程序异常了!");
//            } finally {
//                System.out.println("执行finally!");
//            }
//            System.out.println("执行finally后面的语句!");
//        }
//    }
//
    public static void main(String[] args) {

//        System.out.println(ShellUtils.execCommand("dir").toString());
//        System.out.println(ShellUtils.execCommand("ls -l").toString());
        //System.out.println(ShellUtils.execCommand("ping www.baidu.com").toString());
//        System.out.println(ShellUtils.execCommand("aapt v").toString());
//        System.out.println(ShellUtils.execCommand("aapt.exe").toString());
        ClientShellTask task=new ClientShellTask("ls");
        task.toString();
        ClientThreadManager.getInstance().startNewTask(task.getRunnable(new BaseTask.TaskStateListener() {
            @Override
            public void onBegin() {

            }

            @Override
            public void onFinished() {

            }
        }));

        ClientShellTask task2=new ClientShellTask("ls");
        task2.toString();
        ClientThreadManager.getInstance().startNewTask(task2.getRunnable(new BaseTask.TaskStateListener() {
            @Override
            public void onBegin() {

            }

            @Override
            public void onFinished() {

            }
        }));

    }



}

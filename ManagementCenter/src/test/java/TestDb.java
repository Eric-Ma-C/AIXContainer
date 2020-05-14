import org.zju.vipa.aix.container.center.db.DbManager;
import org.zju.vipa.aix.container.center.db.entity.Task;
import org.zju.vipa.aix.container.center.db.entity.DataturksUser;

import java.util.List;

/**
 * @Date: 2020/1/9 21:43
 * @Author: EricMa
 * @Description: 测试
 */
public class TestDb {
        public static void main(String[] args) {
//               getUserList();
                getTaskList();
                System.out.println("\n\n\n");
                getWaittingTaskList();
        }

        private static void getDataturksUserList(){
                List<DataturksUser> dataturksUserList =DbManager.getInstance().getDataturksUserList();
                for (DataturksUser dataturksUser : dataturksUserList) {
                        System.out.println(dataturksUser.toString());
                }
        }

        private static void getTaskList(){
                List<Task> taskList=DbManager.getInstance().getTaskList();
                for (Task task : taskList) {
                        System.out.println(task.toString());
                }
        }

        private static void getWaittingTaskList(){
                List<Task> taskList=DbManager.getInstance().getWaittingTaskList();
                for (Task task : taskList) {
                        System.out.println(task.toString());
                }
        }

        /** 向数据库写入测试device */
        public static void initTestDevices(){

        }
}

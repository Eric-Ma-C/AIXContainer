import org.zju.vipa.aix.container.center.db.DbManager;
import org.zju.vipa.aix.container.common.db.entity.aix.DataturksUser;
import org.zju.vipa.aix.container.common.db.entity.aix.Task;

import java.util.List;

/**
 * @Date: 2020/1/9 21:43
 * @Author: EricMa
 * @Description: 测试
 */
public class TestDb {
        public static void main(String[] args) {
//               getUserList();
//                getClientIdByToken("gfd");
                System.out.println("\n\n\n");
//                getTaskList();
                updateDeviceDetail();
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

        private static void getClientIdByToken(String token){
                String id = DbManager.getInstance().getClientIdByToken(token);

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

//        @Test
        public static void updateDeviceDetail(){
                DbManager.getInstance().updateDeviceGpuDetailById("2","{\n" +
                    "                \"driverVersion\": \"440.31\",\n" +
                    "                \"cudaVersion\": \"10.2\",\n" +
                    "                \"gpuNum\": 2,\n" +
                    "                \"gpus\": [\n" +
                    "                    {\n" +
                    "                        \"id\": \"0\",\n" +
                    "                        \"name\": \"Quadro P5000\",\n" +
                    "                        \"temperature\": \"65 C\",\n" +
                    "                        \"powerDraw\": \"62.64 W\",\n" +
                    "                        \"powerLimit\": \"180.00 W\",\n" +
                    "                        \"memUsed\": \"5541 MiB\",\n" +
                    "                        \"memAll\": \"16276 MiB\",\n" +
                    "                        \"processes\": []\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"id\": \"1\",\n" +
                    "                        \"name\": \"Tesla K20c\",\n" +
                    "                        \"temperature\": \"47 C\",\n" +
                    "                        \"powerDraw\": \"50.99 W\",\n" +
                    "                        \"powerLimit\": \"225.00 W\",\n" +
                    "                        \"memUsed\": \"0 MiB\",\n" +
                    "                        \"memAll\": \"4743 MiB\",\n" +
                    "                        \"processes\": []\n" +
                    "                    }\n" +
                    "                ]\n" +
                    "            }");
        }
}

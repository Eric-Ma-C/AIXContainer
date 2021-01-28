import org.zju.vipa.aix.container.center.db.AtlasDbManager;
import org.zju.vipa.aix.container.common.db.entity.aix.DataturksUser;
import org.zju.vipa.aix.container.common.db.entity.aix.Task;

import java.util.List;

/**
 * @Date: 2020/1/9 21:43
 * @Author: EricMa
 * @Description: 测试
 */
public class TestDb {

        /** 改为WAITING */
        public static void main(String[] args) {
//                boolean b = AtlasDbManager.getInstance().updateDeviceNameById("1", "c11");
//                System.out.println(b);
                AtlasDbManager.getInstance().setTaskWaitingById("291");
//                AtlasDbManager.getInstance().setTaskWaitingById("227");
//                DbManager.getInstance().setTaskWaitingById("221");


//                AixDevice client = AtlasDbManager.getInstance().getClientById(String.valueOf(2));
//                System.out.println(client);

//                FinishedTask finishedTask = new FinishedTask("123", "312", "status",new Date(),new Date(),new ArrayList<String>());
//                FinishedTask finishedTask = new FinishedTask(ManagementCenter.getInstance().getClientIdByToken(token),
//                    task.getId(), taskStatus, task.getStartTime(), new Date(),
//                    ManagementCenter.getInstance().getTaskLogsByToken(token));
//                AixDbManager.getInstance().insertFinishedTask(finishedTask);

//                AtlasDbManager.getInstance().updateDeviceGpuDetailById("1","success");
        }

//        public static void main(String[] args) {
////               getUserList();
////                getClientIdByToken("gfd");
////                System.out.println("\n\n\n");
////                getTaskList();
////                updateDeviceDetail();
//
//                DbManager.getInstance().grabTask("2");
//                try {
//                        Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                        e.printStackTrace();
//                }
//                DbManager.getInstance().grabTask("3");
//        }

        private static void getDataturksUserList(){
                List<DataturksUser> dataturksUserList = AtlasDbManager.getInstance().getDataturksUserList();
                for (DataturksUser dataturksUser : dataturksUserList) {
                        System.out.println(dataturksUser.toString());
                }
        }

        private static void getTaskList(){
                List<Task> taskList= AtlasDbManager.getInstance().getTaskList();
                for (Task task : taskList) {
                        System.out.println(task.toString());
                }
        }

        private static void getClientIdByToken(String token){
                String id = AtlasDbManager.getInstance().getClientIdByToken(token);

        }

        private static void getWaittingTaskList(){
                List<Task> taskList= AtlasDbManager.getInstance().getWaittingTaskList();
                for (Task task : taskList) {
                        System.out.println(task.toString());
                }
        }

        /** 向数据库写入测试device */
        public static void initTestDevices(){

        }

//        @Test
        public static void updateDeviceDetail(){
                AtlasDbManager.getInstance().updateDeviceGpuDetailById("2","{\n" +
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

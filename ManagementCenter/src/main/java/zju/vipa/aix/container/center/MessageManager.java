package zju.vipa.aix.container.center;

import com.sun.org.apache.xerces.internal.impl.dtd.models.DFAContentModel;
import zju.vipa.aix.container.center.db.DbManager;
import zju.vipa.aix.container.center.db.entity.Task;
import zju.vipa.aix.container.message.Intent;
import zju.vipa.aix.container.message.Message;
import zju.vipa.aix.container.network.NetworkConfig;

import java.util.*;

/**
 * @Date: 2020/3/18 14:55
 * @Author: EricMa
 * @Description: 管理发送给容器的消息
 * 给容器派发任务
 */
public class MessageManager {

    /**
     * 消息队列
     */
    private Map<String, LinkedList<Message>> messageMap;
//    private Queue<SeverMessage> messageQueue;

    private static class TaskManagerHolder {
        private static final MessageManager INSTANCE = new MessageManager();
    }

    private MessageManager() {
        messageMap = new HashMap<>();

        //todo test code
        Message msg = new Message(Intent.SHELL_TASK, "source /root/miniconda3/bin/activate clean_yolo && python  /nfs2/sontal/codes/TrainerProxy/main.py");
        addMessage(NetworkConfig.TEST_CONTAINER_ID, msg);
    }

    public static MessageManager getInstance() {
        return TaskManagerHolder.INSTANCE;
    }


    /**
     * 获取一条对应容器id的待发送消息
     *
     * @param id
     * @return: zju.vipa.aix.container.message.Message
     */
    public Message getMessageById(String id) {

        List<Task> taskList = DbManager.getInstance().getWaittingTaskList();


        LinkedList<Message> messageList = messageMap.get(id);

        Message message=null;
        if (messageList != null) {
            message= messageList.poll();
            messageMap.put(id,messageList);
        }


        return message;
    }



    /**
     * 新增消息
     */
    public void addMessage(String id, Message msg) {

        LinkedList<Message> messageList = messageMap.get(id);
        if (messageList == null) {
            messageList = new LinkedList<>();
        }
        messageList.add(msg);
        messageMap.put(id, messageList);
    }


//    private class SeverMessage{
//        private String clientId;
//        private Message message;
//
//        public SeverMessage(String clientId, Message message) {
//            this.clientId = clientId;
//            this.message = message;
//        }
//
//        public String getClientId() {
//            return clientId;
//        }
//
//        public Message getMessage() {
//            return message;
//        }
//    }
}

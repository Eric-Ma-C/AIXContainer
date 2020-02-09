package zju.vipa.container.message;


/**
 * @Date: 2020/1/9 22:02
 * @Author: EricMa
 * @Description: 消息意图,主题
 */
public enum Intent {

        /** 注册 */
        register("register"),
        /** deamon轮询 */
        deamonQuery("deamonQuery"),



        /** 请求conda环境文件 */
        getCondaEnvFileByTaskId("getCondaEnvFileByTaskId"),
        /** 返回conda环境文件url */
        condaEnvFileUrl("condaEnvFileUrl"),
        /** 请求任务代码 */
        getTaskCode("getTaskCode"),
        /** 返回任务代码文件url */
        taskCodeUrl("taskCodeUrl"),
        /** 请求任务数据 */
        getTaskData("getTaskData"),
        /** 返回任务数据文件url */
        taskDataUrl("taskDataUrl"),


        /** shell指令执行信息 */
        shellInfo("shellInfo"),
        /** shell指令执行错误信息 */
        shellError("shellError"),
        /** shell指令执行结果 */
        shellResult("shellResult");
//        /** conda环境配置错误信息 */
//        condaError("condaError"),
//        /** conda环境配置错误信息 */
//        condaError("condaError"),
//        /** conda环境配置错误信息 */
//        condaError("condaError"),


        private String action;


        private Intent(String action) {
                this.action=action;
        }

        public String getAction() {
                return action;
        }

        @Override
        public String toString() {
                return action;
        }
}

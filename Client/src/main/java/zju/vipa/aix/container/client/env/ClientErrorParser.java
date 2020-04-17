package zju.vipa.aix.container.client.env;

import zju.vipa.aix.container.client.utils.ClientLogUtils;
import zju.vipa.aix.container.config.AIXEnvConfig;

/**
 * @Date: 2020/4/8 21:18
 * @Author: EricMa
 * @Description: 容器python错误解析器
 */
public class ClientErrorParser {
    /**
     * 尝试解析错误，解析成功返回true，不需要平台再处理
     */
    public static String handleModuleNotFoundError(String value) {
        if (value == null) {
            return null;
        }

        if (value.contains("ModuleNotFoundError: No module named")) {

            /** 自动安装一些conda库 */
            String promptName = value.substring(value.indexOf("named") + 7, value.length() - 1);
            String moduleName = getModuleNameByPrompt(promptName);

            ClientLogUtils.info("发现ModuleNotFoundError，module=" + moduleName);

            return moduleName;
        }


        return null;

    }

    /**
     * 提示ModuleNotFoundError: No module named xxx
     * 有时候包名不一致
     */
    private static String getModuleNameByPrompt(String promptName) {
        String moduleName = promptName;
        if ("cv2".equals(moduleName)) {
            moduleName = "opencv-python";
        }

        return moduleName;
    }
}

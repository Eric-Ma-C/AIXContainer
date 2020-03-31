package zju.vipa.aix.container.client.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @Date: 2020/1/11 19:42
 * @Author: EricMa
 * @Description: shell指令执行工具,主要执行快速返回的命令，作用类似RealtimeProcess
 */
public class ShellUtils {


    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static String COMMAND_SH = "sh";
    private static final String COMMAND_EXIT = "exit\n";
    private static final String COMMAND_LINE_END = "\n";

    static {
        if (System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1) {
//            System.out.println("window");
            COMMAND_SH = "cmd";
        } else {
//            System.out.println("unix");
        }

    }



    public static CommandResult execCommand(String command) {
        return execCommand(new String[]{command}, true);
    }

    public static CommandResult execCommand(String command, boolean isNeedResultMsg) {
        return execCommand(new String[]{command}, isNeedResultMsg);
    }

    public static CommandResult execCommand(List<String> commands, boolean isNeedResultMsg) {
        return execCommand(commands == null ? null : commands.toArray(new String[]{}), isNeedResultMsg);
    }

    /**
     * execute shell commands
     * {@link CommandResult#result} is -1, there maybe some excepiton.
     *
     * @param commands     command array
     * @param isNeedResultMsg whether need result msg
     */
    public static CommandResult execCommand(String[] commands, final boolean isNeedResultMsg) {
        int result = -1;
        if (commands == null || commands.length == 0) {
            return new CommandResult(result, null, "空命令");
        }

        Process process = null;

        final StringBuilder successMsg = new StringBuilder();
        final StringBuilder errorMsg = new StringBuilder();

        DataOutputStream outputStream = null;
        try {
            process = Runtime.getRuntime().exec(COMMAND_SH);
            outputStream = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) {
                    continue;
                }
                // donnot use os.writeBytes(commmand), avoid chinese charset error
                outputStream.write(command.getBytes());
                outputStream.writeBytes(COMMAND_LINE_END);
                outputStream.flush();
            }
            outputStream.writeBytes(COMMAND_EXIT);
            outputStream.flush();

            final BufferedReader successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            final BufferedReader errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            //启动新的线程,解决process.waitFor()阻塞问题
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        if (isNeedResultMsg) {
                            String successTmp=null,errorTmp = null;
                            while ((successTmp = successResult.readLine()) != null || (errorTmp = errorResult.readLine()) != null) {
                                if (successTmp!=null) {
                                    successMsg.append(successTmp);
                                    successMsg.append(LINE_SEPARATOR);
                                }
                                if (errorTmp!=null){
                                    errorMsg.append(errorTmp);
                                    errorMsg.append(LINE_SEPARATOR);
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            result = process.waitFor();
            if (errorResult != null) {
                errorResult.close();
            }
            if (successResult != null) {
                successResult.close();
            }

        } catch (Exception e) {
            ClientExceptionUtils.handle(e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                ClientExceptionUtils.handle(e);
            } finally {
                if (process != null) {
                    process.destroy();
                }
            }

        }
        return new CommandResult(result, successMsg == null ? null : successMsg.toString(), errorMsg == null ? null
            : errorMsg.toString());
    }

    public static class CommandResult {

        public int result;
        public String responseMsg;
        public String errorMsg;

        public CommandResult(int result) {
            this.result = result;
        }

        public CommandResult(int result, String responseMsg, String errorMsg) {
            this.result = result;
            this.responseMsg = responseMsg;
            this.errorMsg = errorMsg;
        }

        @Override
        public String toString() {
            return "CommandResult{" +
                "result=" + result +
                ", responseMsg='" + responseMsg + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
        }
    }

}

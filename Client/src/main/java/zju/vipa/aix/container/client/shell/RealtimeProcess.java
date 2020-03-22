package zju.vipa.aix.container.client.shell;

import zju.vipa.aix.container.client.utils.ClientExceptionUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @Date: 2020/1/11 21:45
 * @Author: EricMa
 * @Description:
 */

public class RealtimeProcess {
    /**
     * 是否在执行
     */
    private boolean isRunning = false;
    /**
     * 存放命令行
     */
    private RealtimeProcessCommand mRealtimeProcessCommand = new RealtimeProcessCommand();
    /**
     * 保存所有的输出信息
     */
    private StringBuffer mStringBuffer = new StringBuffer();
    /**
     * 处理线程构造器
     */
    private ProcessBuilder mProcessBuilder = null;
    private BufferedReader readStdout = null;
    private BufferedReader readStderr = null;
    /**
     * 回调用到的接口
     */
    private RealtimeProcessInterface mInterface = null;
    private int resultCode = 0;
    private String execDir = null;
    private String tmp1 = null;
    private String tmp2 = null;


    public RealtimeProcess(RealtimeProcessInterface mInterface) {
        // 实例化接口对象
        this.mInterface = mInterface;
    }

    public void setCommand(String commands) {
        // 遍历命令
//        for(String cmd : commands){
//            RealtimeProcessCommand mRealtimeProcessCommand = new RealtimeProcessCommand();
//        if (ROOT_DIR != null) {
//            mRealtimeProcessCommand.setDirectory(ROOT_DIR);
//        }
        mRealtimeProcessCommand.setCmdWords(commands);

//        }
    }

    public String getExecDir() {
        return execDir;
    }

    public void setExecDir(String execDir) {
        this.execDir = execDir;
    }

    public void start() throws IOException, InterruptedException {
        isRunning = true;
        mInterface.onProcessBegin(mRealtimeProcessCommand.getCmdWords());
//        for(RealtimeProcessCommand mRealtimeProcessCommand : commandList){
//        mProcessBuilder = new ProcessBuilder(partitionCommandLine(mRealtimeProcessCommand.getCmdWords()));
        mProcessBuilder = new ProcessBuilder("/bin/bash","-c",mRealtimeProcessCommand.getCmdWords());

        mProcessBuilder.directory(new File(execDir));
//        mProcessBuilder.directory(new File(System.getenv("HOME")));


        // 不重定向错误输出
        mProcessBuilder.redirectErrorStream(false);

        exec(mProcessBuilder.start());
//        }
    }

    public static String[] partitionCommandLine(final String command) {
        final ArrayList<String> commands = new ArrayList<>();

        int index = 0;

        StringBuffer buffer = new StringBuffer(command.length());

        boolean isApos = false;
        boolean isQuote = false;
        while (index < command.length()) {
            final char c = command.charAt(index);

            switch (c) {
                case ' ':
                    if (!isQuote && !isApos) {
                        final String arg = buffer.toString();
                        buffer = new StringBuffer(command.length() - index);
                        if (arg.length() > 0) {
                            commands.add(arg);
                        }
                    } else {
                        buffer.append(c);
                    }
                    break;
                case '\'':
                    if (!isQuote) {
                        isApos = !isApos;
                    } else {
                        buffer.append(c);
                    }
                    break;
                case '"':
                    if (!isApos) {
                        isQuote = !isQuote;
                    } else {
                        buffer.append(c);
                    }
                    break;
                default:
                    buffer.append(c);
            }

            index++;
        }

        if (buffer.length() > 0) {
            final String arg = buffer.toString();
            commands.add(arg);
        }
        return commands.toArray(new String[commands.size()]);
    }

    public String getAllResult() {
        return mStringBuffer.toString();
    }

    private void exec(final Process process) throws InterruptedException {
        // 获取标准输出
        readStdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
        // 获取错误输出
        readStderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));



        // todo 线程池 创建线程执行
        Thread execThread = new Thread() {
            @Override
            public void run() {
                try {
                    // 逐行读取
                    while ((tmp1 = readStdout.readLine()) != null || (tmp2 = readStderr.readLine()) != null) {
                        if (tmp1 != null) {
                            mStringBuffer.append(tmp1 + "\n");
                            // 回调接口方法
                            mInterface.onNewStdoutListener(tmp1);
                        }
                        if (tmp2 != null) {
                            mStringBuffer.append(tmp2 + "\n");
                            mInterface.onNewStderrListener(tmp2);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                resultCode = process.exitValue();
                try {
                    resultCode = process.waitFor();
                } catch (InterruptedException e) {
                    ClientExceptionUtils.handle(e);
                }
            }
        };
        execThread.start();
        execThread.join();
        isRunning = false;
        mInterface.onProcessFinish(resultCode);
    }

    public boolean isRunning() {
        return this.isRunning;
    }

}



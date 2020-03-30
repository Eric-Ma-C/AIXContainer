package zju.vipa.aix.container.client.shell;

import zju.vipa.aix.container.client.thread.ClientThreadManager;
import zju.vipa.aix.container.client.utils.ClientExceptionUtils;
import zju.vipa.aix.container.utils.ExceptionUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

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
    private String stdOutTmp = null;
    private String stdErrTmp = null;


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

    public void start() throws IOException {
        isRunning = true;
        mInterface.onProcessBegin(mRealtimeProcessCommand.getCmdWords());

//        mProcessBuilder = new ProcessBuilder(partitionCommandLine(mRealtimeProcessCommand.getCmdWords()));
        mProcessBuilder = new ProcessBuilder("/bin/bash", "-c", mRealtimeProcessCommand.getCmdWords());

        mProcessBuilder.directory(new File(execDir));
//        mProcessBuilder.directory(new File(System.getenv("HOME")));


        // 不重定向错误输出
        mProcessBuilder.redirectErrorStream(false);
        /** 启动process */
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


    /**
     * 在新的线程中处理命令输出，防止io阻塞命令执行线程
     *
     * @param process 任务执行线程
     * @return: void
     */
    private void exec(final Process process) throws IOException {
        // 获取标准输出
        readStdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
        // 获取错误输出
        readStderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        final CountDownLatch latch = new CountDownLatch(1);
        ClientThreadManager.getInstance().startNewTask(new Runnable() {
            @Override
            public void run() {
                try {
                    // 逐行读取,readLine()只有在数据流发生异常或者另一端被close()掉时，才会返回null值
                    while ((stdOutTmp = readStdout.readLine()) != null || (stdErrTmp = readStderr.readLine()) != null) {
                        if (stdOutTmp != null) {
                            mStringBuffer.append(stdOutTmp + "\n");
                            // 回调接口方法
                            mInterface.onNewStdoutListener(stdOutTmp);
                        }
                        if (stdErrTmp != null) {
                            mStringBuffer.append(stdErrTmp + "\n");
                            mInterface.onNewStderrListener(stdErrTmp);
                        }
                    }
                } catch (IOException e) {
                    ClientExceptionUtils.handle(e);
                }
//                resultCode = process.exitValue();
                try {
                    resultCode = process.waitFor();
                } catch (InterruptedException e) {
                    ClientExceptionUtils.handle(e);
                } finally {
                    if (process != null) {
                        process.destroy();
                    }

                    latch.countDown();
                }
            }

        });
//        Thread handleOutputThread = new Thread() {
//
//        };

//        handleOutputThread.start();
        try {
//            handleOutputThread.join();
            latch.await();
        } catch (InterruptedException e) {
            ClientExceptionUtils.handle(e);
        } finally {
            if (readStderr != null) {
                readStderr.close();
            }
            if (readStdout != null) {
                readStdout.close();
            }
        }


        isRunning = false;
        mInterface.onProcessFinish(resultCode);
    }

    public boolean isRunning() {
        return this.isRunning;
    }

}



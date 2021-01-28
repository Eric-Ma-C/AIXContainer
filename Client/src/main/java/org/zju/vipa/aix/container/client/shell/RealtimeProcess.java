package org.zju.vipa.aix.container.client.shell;

import org.zju.vipa.aix.container.client.thread.ClientThreadManager;
import org.zju.vipa.aix.container.client.utils.ClientExceptionUtils;
import org.zju.vipa.aix.container.client.utils.ClientLogUtils;
import org.zju.vipa.aix.container.client.utils.ShellUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
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
    private Process mProcess;
    private BufferedReader readStdout = null;
    private BufferedReader readStderr = null;
    /**
     * 回调用到的接口
     */
    private RealtimeProcessListener mInterface = null;
    private int resultCode = 0;
    private String execDir = null;
    private String stdOutTmp = null;
    private String stdErrTmp = null;


    public RealtimeProcess(RealtimeProcessListener mInterface) {
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
        /** todo 在当前shell下执行,不启动子shell？ */
//        mProcessBuilder = new ProcessBuilder("source", mRealtimeProcessCommand.getCmdWords());

        mProcessBuilder.directory(new File(execDir));
//        mProcessBuilder.directory(new File(System.getenv("HOME")));


        // 不重定向错误输出
        mProcessBuilder.redirectErrorStream(false);

        /** 启动process */
        mProcess=mProcessBuilder.start();
        handleProcessOutput(mProcess);

        //test env
//        ClientLogUtils.info(mProcessBuilder.environment().toString(), true);
//        ClientLogUtils.info("System.getProperties() "+System.getProperties().toString(), true);
//        ClientLogUtils.info("System.getenv() "+System.getenv().toString(), true);
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
    private void handleProcessOutput(final Process process) throws IOException {
        // 获取标准输出
        readStdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
        // 获取错误输出
        readStderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        final CountDownLatch latch = new CountDownLatch(1);
        ClientThreadManager.getInstance().startNewTask(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("process output");
                try {
                    // 逐行读取,readLine()只有在数据流发生异常或者另一端被close()掉时，才会返回null值
                    while ((stdOutTmp = readStdout.readLine()) != null || (stdErrTmp = readStderr.readLine()) != null) {
                        if (stdOutTmp != null) {
                            mStringBuffer.append(stdOutTmp + "\n");
                            // 回调接口方法
                            mInterface.onNewStdOut(stdOutTmp);
                        }
                        if (stdErrTmp != null) {
                            mStringBuffer.append(stdErrTmp + "\n");
                            mInterface.onNewStdError(stdErrTmp);
                        }
                    }
                } catch (IOException e) {
                    ClientExceptionUtils.handle(e);
                }
//                resultCode = process.exitValue();
                try {

                    ClientLogUtils.debug("process.waitFor()调用");
                    resultCode = process.waitFor();
                    ClientLogUtils.debug("process.waitFor()返回resultCode={}",resultCode);




                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    ClientExceptionUtils.handle(e);
                } finally {
                    if (process != null) {
                        process.destroy();
                    }

                    latch.countDown();
                }
            }

        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            resultCode=-1;
//            ClientExceptionUtils.handle(e);
        } finally {
            /** 关闭IO流 */
            if (readStderr != null) {
                readStderr.close();
            }
            if (readStdout != null) {
                readStdout.close();
            }
        }


        isRunning = false;
        mInterface.onProcessFinished(resultCode);
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public void stop(){
        if (mProcess != null) {
//            ClientLogUtils.debug("mProcess.destroy()调用");
//            mProcess.destroy();
            long pid = getPidOfProcess(mProcess);

            String[] childPids = getChildPids(pid);
            if (childPids!=null) {
                for (String childPid : childPids) {
                    if ("".equals(childPid)) {
                        continue;
                    }
                    /** kill所有子进程 */
                    killProcessByPid(Long.valueOf(childPid));
                }
            }
            /** kill mProcess进程 */
            killProcessByPid(pid);
        }
    }

    private void killProcessByPid(long pid){
        if (pid<=0){
            ClientLogUtils.error("error kill -9 {}",pid);
            return;
        }

        ClientLogUtils.debug("kill -9 {}",pid);
        ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", "kill -9 "+pid);
        try {
            Process p = pb.start();
        } catch (IOException e) {
            ClientExceptionUtils.handle(e);
        }
    }

    public static String[] getChildPids(long pid) {
//        ShellUtils.CommandResult result1 = ShellUtils.execCommand("ps --ppid "+pid);
//        System.out.println(result1);
        ShellUtils.CommandResult result = ShellUtils.execCommand("ps --ppid "+pid+"|awk '{print $1}'|awk 'NR==1{next}{print}'");


        if (result.result != 0) {
            ClientLogUtils.error(result.errorMsg);
            return null;
        }
        String pids = result.responseMsg;
        String[] pidStrs = pids.split("\n");
        ClientLogUtils.info("getChildPids({})={}",pid, Arrays.asList(pidStrs));
        return pidStrs;
    }


    private synchronized long getPidOfProcess(Process p) {
        long pid = -1;

        try {
            if (p.getClass().getName().equals("java.lang.UNIXProcess")) {
                Field f = p.getClass().getDeclaredField("pid");
                f.setAccessible(true);
                pid = f.getLong(p);
                f.setAccessible(false);

            }
        } catch (Exception e) {
            pid = -1;
        }
        return pid;
    }

}



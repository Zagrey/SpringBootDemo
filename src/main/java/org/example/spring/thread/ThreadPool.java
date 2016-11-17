package org.example.spring.thread;

import org.example.spring.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import java.util.concurrent.*;

//@Component
public class ThreadPool {

    @Autowired
    private Environment env;
    @Value("${spring.rabbitmq.listener.prefetch:123}")
    Integer poolSizeProp;

    Logger LOGGER = LoggerFactory.getLogger(ThreadPool.class);

    private final long HEARTBEAT_TIMEOUT_SEC = 10;

    private final ExecutorService tpExecutor;
    private final ScheduledExecutorService heartBeatService;

    public static class SingletonHolder {
        public static final ThreadPool HOLDER_INSTANCE = new ThreadPool();
    }

    private ThreadPool() {
        // thread pool size depends on prefetch RMQ
        // we do in parallel maximum number of tasks == prefetch
        int poolSize = 10;//Integer.parseInt(env.getProperty("spring.rabbitmq.listener.prefetch"));
        if (poolSize <= 0) {
            poolSize = 10;
        }

        tpExecutor = Executors.newFixedThreadPool(poolSize);
        heartBeatService = Executors.newSingleThreadScheduledExecutor();
        startHeartbeatThread();

        LOGGER.info("Thread Initialized of size " + poolSize + "\n");
    }

    public static ThreadPool getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    public void submitTask(Task t) {
        tpExecutor.submit(t);
    }

    public void shutdownThreadPool() {
        LOGGER.warn("Thread Pool Windows Service going down...");

        tpExecutor.shutdown();

        // Wait for termination
        boolean success = false;
        try {
            success = tpExecutor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!success) {
            tpExecutor.shutdownNow();
        }

    }

    private void startHeartbeatThread() {
        heartBeatService.scheduleAtFixedRate(
                () -> {
                    try {
                        LOGGER.info("[Heartbeat-TP]: "
                                + "alive=" + (tpExecutor.isShutdown() ? "f" : "t") + ", "
                                + "active=" +  ((ThreadPoolExecutor) tpExecutor).getActiveCount() + ", "
                                + "completed=" +  ((ThreadPoolExecutor) tpExecutor).getCompletedTaskCount() + ", "
                                + "Total_Mem=" + (int) ((double) Runtime.getRuntime().totalMemory() / (1024 * 1024)) + "Mb, "
                                + "Free_Mem=" + (int) ((double) Runtime.getRuntime().freeMemory() / (1024 * 1024))  + "Mb"
                        );
                    } catch (Exception e) {
                        LOGGER.error("Unable to write heartbeat message:", e);
                        e.printStackTrace();
                    }
                }, HEARTBEAT_TIMEOUT_SEC, HEARTBEAT_TIMEOUT_SEC, TimeUnit.SECONDS);
    }
}

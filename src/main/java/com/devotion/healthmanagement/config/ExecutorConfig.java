package com.devotion.healthmanagement.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@EnableAsync
@Configuration
public class ExecutorConfig {

    private static final int corePoolSize = 5;   // 核心线程数（默认线程数）
    private static final int maxPoolSize = 10;   // 最大线程数
    private static final int keepAliveTime = 60;  // 允许线程空闲时间（单位：默认为秒）
    private static final int queueCapacity = 20; // 缓冲队列数

    @Bean("asyncServiceExecutor")
    public ThreadPoolTaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setThreadNamePrefix("Upload Thread-");
        pool.setCorePoolSize(corePoolSize);
        pool.setMaxPoolSize(maxPoolSize);
        pool.setKeepAliveSeconds(keepAliveTime);
        pool.setQueueCapacity(queueCapacity);
        // 直接在execute方法的调用线程中运行
        pool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        pool.initialize();
        return pool;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }
}


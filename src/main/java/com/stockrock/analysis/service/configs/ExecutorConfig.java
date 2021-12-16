package com.stockrock.analysis.service.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableAsync
public class ExecutorConfig {

    public static final String THREAD_POOL = "threadPoolExecutor";
    public static final String CONCURRENT_THREAD = "ConcurrentTaskExecutor";
    public static final String SINGLE_THREAD = "SingleTaskExecutor";

   @Bean(name = THREAD_POOL)
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(7);
        executor.setMaxPoolSize(42);
        executor.setQueueCapacity(11);
        executor.setThreadNamePrefix("threadPoolExecutor-");
        executor.initialize();
        return executor;
    }

    @Bean(name = CONCURRENT_THREAD)
    public TaskExecutor taskExecutor2 () {
        return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(3));
    }

    @Bean(name = SINGLE_THREAD)
    public TaskExecutor taskExecutor3 () {
        return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(1));
    }

}
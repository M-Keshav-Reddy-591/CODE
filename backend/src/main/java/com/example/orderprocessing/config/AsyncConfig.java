package com.example.orderprocessing.config;

import org.springframework.context.annotation.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {
  @Bean(name="orderExecutor") Executor orderExecutor(){
    ThreadPoolTaskExecutor executor=new ThreadPoolTaskExecutor(); executor.setCorePoolSize(5); executor.setMaxPoolSize(10); executor.setQueueCapacity(100); executor.setThreadNamePrefix("order-worker-"); executor.initialize(); return executor;
  }
}

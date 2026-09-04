package com.mentoai.mentoaiapi.analysis.infrastructure.config;

import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;

@Configuration
@EnableAsync
public class AnaliseAsyncConfig {

    @Bean("analysisExecutor")
    public ThreadPoolTaskExecutor analysisExecutor(
            @Value("${mentoai.analysis.async.pool-size}") int poolSize,
            @Value("${mentoai.analysis.async.queue-capacity}") int queueCapacity) {
        Assert.isTrue(poolSize > 0, "mentoai.analysis.async.pool-size deve ser positivo");
        Assert.isTrue(queueCapacity > 0, "mentoai.analysis.async.queue-capacity deve ser positiva");

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolSize);
        executor.setMaxPoolSize(poolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("analysis-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        return executor;
    }
}

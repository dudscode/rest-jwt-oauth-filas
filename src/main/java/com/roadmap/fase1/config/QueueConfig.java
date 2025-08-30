package com.roadmap.fase1.config;

import com.roadmap.fase1.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class QueueConfig {
    @Bean
    public BlockingQueue<User> userQueue (){ return new LinkedBlockingQueue<>(5);}
}

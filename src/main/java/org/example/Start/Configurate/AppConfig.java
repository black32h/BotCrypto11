package org.example.Configurate;

import org.example.Bot.LaunchPoolBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling // Вмикаємо планувальник
@ComponentScan(basePackages = "org.example")
public class AppConfig {
    @Bean
    public LaunchPoolBot launchPoolBot() {
        return new LaunchPoolBot();
    }
}
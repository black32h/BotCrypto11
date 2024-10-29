package org.example.MyScheduler;

import org.example.Bot.LaunchPoolBot;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MyScheduler {

    private final LaunchPoolBot launchPoolBot;

    public MyScheduler(LaunchPoolBot launchPoolBot) {
        this.launchPoolBot = launchPoolBot;
    }

    @Scheduled(fixedRate = 21600000) // Кожні 6 годин (21600000 мілісекунд)
    public void scheduleFixedRateTask() {
        launchPoolBot.notifyActivePools(); // Викликаємо метод для сповіщення про активні пули
    }
}
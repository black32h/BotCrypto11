package org.example.Bot;

import org.example.ReaderModel.LaunchPoolInfo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class LaunchPoolBot extends TelegramLongPollingBot {

    private List<LaunchPoolInfo> pools;
    private Set<String> notifiedPools = new HashSet<>();
    private final String cacheFilePath = "C:\\Users\\Админ\\IdeaProjects\\BotCrypto1\\src\\main\\resources\\cash.txt";

    @Override
    public void onUpdateReceived(Update update) {
        // Логика обработки обновлений
    }

    @Override
    public String getBotUsername() {
        return "Cryptouser_73bot"; // имя бота
    }

    @Override
    public String getBotToken() {
        return "7645000805:AAHq2WHmEOGTncILPMZ9ThRg1k8-pVBySnY"; // токен бота
    }

    public void setPools(List<LaunchPoolInfo> pools) {
        this.pools = pools;
    }

    public void printPools() {
        if (pools == null || pools.isEmpty()) {
            System.out.println("Список пулов пуст.");
            return;
        }

        StringBuilder message = new StringBuilder();
        message.append("Список пулов:\n");

        for (LaunchPoolInfo pool : pools) {
            message.append(pool.toString()).append("\n");
        }

        System.out.println(message.toString());
    }

    @Scheduled(fixedRate = 14400000)
    public void notifyUpcomingPools() {
        List<LaunchPoolInfo> upcomingPools = getUpcomingPools();
        for (LaunchPoolInfo pool : upcomingPools) {
            if (!notifiedPools.contains(pool.getLaunchPool())) {
                sendMessageToChat(pool.toString());
                notifiedPools.add(pool.getLaunchPool());
            }
        }
    }

    public void notifyActivePools() {
        List<LaunchPoolInfo> activePools = getActivePools();
        for (LaunchPoolInfo pool : activePools) {
            sendMessageToChat(pool.toString());
        }
    }

    public void removeInactivePools() {
        if (pools != null) {
            pools.removeIf(pool -> "Неактивний".equalsIgnoreCase(pool.getStatus()));
            System.out.println("Неактивные пулы удалены из кэша.");
        }
    }

    private void archiveCache() {
        if (pools != null && !pools.isEmpty()) {
            StringBuilder cacheContent = new StringBuilder();

            for (LaunchPoolInfo pool : pools) {
                cacheContent.append(pool.toString()).append(System.lineSeparator());
            }

            try {
                Files.write(Paths.get(cacheFilePath), cacheContent.toString().getBytes());
                System.out.println("Кэш архивирован в файл: " + cacheFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Нет данных для архивирования.");
        }
    }

    @Scheduled(cron = "0 0 12 * * MON")
    public void weeklyMaintenance() {
        archiveCache();
        removeInactivePools();
    }

    @Scheduled(cron = "0 0 12 * * FRI")
    public void publishAllPools() {
        StringBuilder message = new StringBuilder();
        message.append("Активні та майбутні пули:\n");

        List<LaunchPoolInfo> activePools = getActivePools();
        List<LaunchPoolInfo> upcomingPools = getUpcomingPools();

        activePools.forEach(pool -> message.append(pool.toString()).append("\n"));
        upcomingPools.forEach(pool -> message.append(pool.toString()).append("\n"));

        sendMessageToChat(message.toString());
    }

    private List<LaunchPoolInfo> getActivePools() {
        return pools.stream()
                .filter(pool -> {
                    String status = String.valueOf(pool.getStatus());
                    System.out.println("Проверка статуса: " + status); // Отладочный вывод
                    return "Активний".equalsIgnoreCase(status); // Используем сравнение, учитывающее null
                })
                .toList();
    }

    private List<LaunchPoolInfo> getUpcomingPools() {
        return pools.stream()
                .filter(pool -> pool.getStartDateTime().compareTo(getCurrentDateTimePlusHours(3)) > 0)
                .toList();
    }

    private String getCurrentDateTimePlusHours(int hours) {
        // Метод для получения текущей даты и времени с добавлением часов
        return "";
    }

    private void sendMessageToChat(String message) {
        Long chatId = 985667869L;
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(message);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

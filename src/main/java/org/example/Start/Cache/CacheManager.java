package org.example.Cache;

import org.example.ReaderModel.LaunchPoolInfo;
import org.example.enums.PoolStatus;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CacheManager {
    private static final String CACHE_FILE = "C:\\Users\\Админ\\IdeaProjects\\BotCrypto1\\src\\main\\resources\\cash.txt"; // Укажите путь к файлу кеша
    private static final String ARCHIVE_FILE = "C:\\Users\\Админ\\IdeaProjects\\BotCrypto1\\src\\main\\resources\\arhive"; // Укажите путь к файлу архива

    // Метод для обновления статусов пула на основе данных из файла
    public void updatePoolStatuses () {
        List<LaunchPoolInfo> pools = loadPools(); // Загружаем существующие пулы
        // Пример чтения статусов из файла
        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Админ\\IdeaProjects\\BotCrypto1\\src\\main\\resources\\launch_pools.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(","); // Предположим, что данные разделены запятой
                if (parts.length >= 2) {
                    String launchPool = parts[0].trim();
                    String newStatus = parts[1].trim();
                    // Обновляем статус в соответствующем пуле
                    for (LaunchPoolInfo pool : pools) {
                        if (pool.getLaunchPool().equalsIgnoreCase(launchPool)) {
                            pool.updateStatus(newStatus); // Обновляем статус
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Обрабатываем исключение
        }
        // Сохраняем обновленные пулы обратно в кеш
        savePools(pools);
    }

    // Метод для сохранения пулов в кеш
    public void savePools ( List<LaunchPoolInfo> pools ) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CACHE_FILE))) {
            for (LaunchPoolInfo pool : pools) {
                writer.write(pool.toString()); // Записываем строку пула в файл
                writer.newLine(); // Переход на новый ряд
            }
        } catch (IOException e) {
            e.printStackTrace(); // Выводим ошибку, если не удалось сохранить
        }
    }

    // Метод для загрузки пулов из кеша
    public List<LaunchPoolInfo> loadPools () {
        List<LaunchPoolInfo> pools = new ArrayList<>(); // Список для сохранения пулов
        try (BufferedReader reader = new BufferedReader(new FileReader(CACHE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                LaunchPoolInfo pool = parseLaunchPoolInfo(line); // Парсим строку в объект LaunchPoolInfo
                pools.add(pool); // Добавляем пул в список
            }
        } catch (IOException e) {
            e.printStackTrace(); // Выводим ошибку, если не удалось загрузить
        }
        return pools; // Возвращаем список пулов
    }

    // Метод для очистки кеша
    public void clearCache () {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CACHE_FILE))) {
            writer.write(""); // Очищаем файл, записывая пустую строку
        } catch (IOException e) {
            e.printStackTrace(); // Выводим ошибку, если не удалось очистить кеш
        }
    }

    // Метод для архивации пулов
    public void archivePools () {
        try {
            // Читаем существующий кеш и архивируем
            List<LaunchPoolInfo> pools = loadPools(); // Загружаем пулы из кеша
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVE_FILE, true))) {
                for (LaunchPoolInfo pool : pools) {
                    writer.write(pool.toString()); // Записываем пул в файл архива
                    writer.newLine(); // Переход на новый ряд
                }
            }
            clearCache(); // Очищаем кеш после архивирования
        } catch (IOException e) {
            e.printStackTrace(); // Выводим ошибку, если не удалось архивировать
        }
    }

    // Метод для удаления неактивных пулов
    public void removeInactivePools() {
        List<LaunchPoolInfo> pools = loadPools(); // Загружаем пулы из кеша
        List<LaunchPoolInfo> activePools = new ArrayList<>(); // Список для активных пулов

        // Фильтруем неактивные пулы, оставляя только активные
        for (LaunchPoolInfo pool : pools) {
            // Проверяем статус с использованием метода equals
            if (pool.getStatus() == null || !pool.getStatus().equals(PoolStatus.Активний)) {
                continue;
            }
            activePools.add(pool); // Добавляем активный пул в список
        }

        // Сохраняем только активные пулы обратно в кеш
        savePools(activePools); // Сохраняем активные пулы в кеш
    }


    // Метод для парсинга строки в объект LaunchPoolInfo
    private LaunchPoolInfo parseLaunchPoolInfo ( String line ) {
        line = line.trim(); // Обрезаем пробелы
        String[] parts = line.split(","); // Разбиваем строку на части по запятой

        if (parts.length != 5) { // Проверяем, что массив имеет нужное количество элементов
            throw new IllegalArgumentException("Неверный формат строки: " + line);
        }

        // Получаем данные из частей строки
        String exchange = parts[0].trim(); // Название биржи
        String launchPool = parts[1].trim(); // Название пула
        String pools = parts[2].trim(); // Дополнительная информация о пулах
        String period = parts[3].trim(); // Период
        String statusStr = parts[4].trim(); // Статус в строковом формате

        // Преобразуем строку статуса в объект PoolStatus, если возможно
        PoolStatus status;
        try {
            status = PoolStatus.valueOf(statusStr.replace(" ", "_").toUpperCase()); // Преобразуем статус в Enum
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неизвестный статус: " + statusStr);
        }

        return new LaunchPoolInfo(exchange, launchPool, pools, period, status); // Возвращаем новый объект LaunchPoolInfo
    }

}

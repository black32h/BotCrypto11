package org.example.ReaderModel;

import org.example.enums.PoolStatus;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LaunchPoolReader {
    // Метод для загрузки данных о пулах (например, из файла или базы данных)
    public List<LaunchPoolInfo> loadLaunchPools(String filePath) {
        List<LaunchPoolInfo> pools = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Пропускаем первую строку (заголовок)
            reader.readLine(); // Пропустить заголовок
            while ((line = reader.readLine()) != null) {
                LaunchPoolInfo pool = parseLineToLaunchPoolInfo(line); // Изменено на новый метод
                pools.add(pool); // Добавляем созданный объект в список
            }
        } catch (IOException e) {
            e.printStackTrace(); // Обработка исключения, если возникла ошибка чтения файла
            // Если произошла ошибка чтения файла, создаем примеры пулов вручную
            return createSamplePools();
        }
        return pools; // Возвращаем список загруженных пулов
    }

    // Метод для парсинга строки в объект LaunchPoolInfo
    private LaunchPoolInfo parseLineToLaunchPoolInfo(String line) {
        String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"); // Используем регулярное выражение для корректного разбиения строки с кавычками
        if (parts.length != 5) {
            throw new IllegalArgumentException("Неверный формат строки: " + line); // Проверка на корректность формата
        }
        String exchange = parts[0].trim();
        String launchPool = parts[1].trim();
        String pools = parts[2].trim().replace("\"", ""); // Убираем кавычки
        String period = parts[3].trim().replace("\"", ""); // Убираем кавычки
        String status = parts[4].trim().replace("\"", ""); // Убираем кавычки и получаем статус
        System.out.println("Парсим статус: " + status); // Логируем статус для отладки
        PoolStatus poolStatus = PoolStatus.getStatusBasedOnCondition(status); // Получаем статус через метод перечисления
        return new LaunchPoolInfo(exchange, launchPool, pools, period, poolStatus); // Создаем новый объект LaunchPoolInfo
    }

    // Метод для создания примеров пулов
    private List<LaunchPoolInfo> createSamplePools() {
        List<LaunchPoolInfo> pools = new ArrayList<>();
        // Создание объектов LaunchPoolInfo
        pools.add(new LaunchPoolInfo("Bitget", "X", "BTC=, ETH=", "24.10 12:00 — 03.11 12:00 UTC", PoolStatus.Активний));
        pools.add(new LaunchPoolInfo("Bitget", "X", "X=0%", "24.10 12:00 — 03.11 12:00 UTC", PoolStatus.Активний));
        pools.add(new LaunchPoolInfo("Gate", "SFT", "ETH=0%, SFT=0%", "24.10 10:00 — 31.10 10:00 UTC", PoolStatus.Активний));
        pools.add(new LaunchPoolInfo("Gate", "CROS", "CROS=6235.18%, BTC=51.06%", "23.10 11:00 — 13.11 11:00 UTC", PoolStatus.Активний));
        pools.add(new LaunchPoolInfo("Gate", "UNIO", "USDT=20.78%, UNIO=4392.18%", "23.10 10:00 — 02.11 10:00 UTC", PoolStatus.Активний));
        return pools;
    }
}

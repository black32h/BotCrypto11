package org.example;

import org.example.MyScheduler.MyScheduler;
import org.example.Bot.LaunchPoolBot;
import org.example.ReaderModel.LaunchPoolInfo;
import org.example.ReaderModel.LaunchPoolReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Создаем контекст Spring
        ApplicationContext context = new AnnotationConfigApplicationContext(org.example.Configurate.AppConfig.class);

        // Читаем данные из файла
        LaunchPoolReader launchPoolReader = new LaunchPoolReader();
        List<LaunchPoolInfo> pools;
        pools = launchPoolReader.loadLaunchPools("C:\\Users\\Админ\\IdeaProjects\\BotCrypto1\\src\\main\\resources\\launch_pools.txt");

        // Выводим информацию о пулах в консоль
        for (LaunchPoolInfo pool : pools) {
            System.out.println(pool);
        }

        // Инициализируем бота и передаем ему пуллы
        LaunchPoolBot bot = context.getBean(LaunchPoolBot.class);
        bot.setPools(pools);

        // Вывод информации о пулах в Telegram
        bot.printPools();

        // Запускаем планировщик задач
        new MyScheduler(bot); // Если вы используете планировщик в отдельном классе
        System.out.println("Бот успешно запущен!");
    }
}

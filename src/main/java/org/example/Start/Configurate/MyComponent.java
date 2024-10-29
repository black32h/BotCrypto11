package org.example.Configurate;

import org.example.MyScheduler.MyScheduled;
import org.springframework.stereotype.Component;

// Компонент Spring, який може бути автоматично виявлений і керований контейнером Spring
@Component
public class MyComponent {

    // Метод, який виконується за графіком, позначений анотацією MyScheduled
    @MyScheduled(fixedRate = 500000) // Інтервал виконання — 500000 мілісекунд (або 500 секунд)
    public void scheduledTask() {
        // Дія, яка виконується під час виконання запланованого завдання
        System.out.println("Запланированное задание выполняется..."); // Виводить повідомлення в консоль
    }
}
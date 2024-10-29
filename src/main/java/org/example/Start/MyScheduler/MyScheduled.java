package org.example.MyScheduler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Анотація для позначення методів, які повинні виконуватися за графіком
@Retention(RetentionPolicy.RUNTIME) // Зберігає анотацію під час виконання
@Target(ElementType.METHOD) // Дозволяє використовувати цю анотацію лише на методах
public @interface MyScheduled {
    long fixedRate(); // Метод, що повертає фіксований інтервал для виконання
}
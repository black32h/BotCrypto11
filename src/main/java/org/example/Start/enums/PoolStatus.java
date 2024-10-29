package org.example.enums;

public enum PoolStatus {
    Активний("Активний"),
    Скоро_почнеться("Скоро почнеться");

    private final String status;

    PoolStatus ( String status ) {
        this.status = status;
    }

    public String getStatus () {
        return status;
    }

    public static PoolStatus getStatusBasedOnCondition ( String status ) {
        for (PoolStatus poolStatus : PoolStatus.values()) {
            if (poolStatus.name().equalsIgnoreCase(status.replace(" ", "_"))) { // Заменяем пробелы на подчеркивания, если это нужно
                return poolStatus;
            }
        }
        throw new IllegalArgumentException("Неизвестный статус: " + status);
    }
}




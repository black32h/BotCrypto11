package org.example.ReaderModel;

import org.example.enums.PoolStatus;

public class LaunchPoolInfo {
    private String exchange;
    private String launchPool;
    private String pools;
    private String period;
    private PoolStatus status;

    // Конструктор
    public LaunchPoolInfo(String exchange, String launchPool, String pools, String period, PoolStatus status) {
        this.exchange = exchange;
        this.launchPool = launchPool;
        this.pools = pools;
        this.period = period;
        this.status = status;
    }

    // Геттеры
    public String getExchange() {
        return exchange;
    }

    public String getLaunchPool() {
        return launchPool;
    }

    public String getPools() {
        return pools;
    }

    public String getPeriod() {
        return period;
    }

    public String getStatus() {
        return String.valueOf(status);
    }

    // Метод для изменения статуса
    public void setStatus(PoolStatus status) {
        this.status = status;
    }

    public void updateStatus(String newStatus) {
        this.status = PoolStatus.getStatusBasedOnCondition(newStatus);
    }

    @Override
    public String toString() {
        return String.format("%s,%s,\"%s\",\"%s\",%s", exchange, launchPool, pools, period, status.getStatus());
    }

    // Метод для получения начальной даты и времени
    public String getStartDateTime() {
        if (period == null || period.isEmpty()) {
            return "";
        }
        String[] parts = period.split(" — ");
        return parts.length > 0 ? parts[0] : "";
    }
}


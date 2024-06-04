package com.sibertech.lib.db.measure;

import lombok.Data;

@Data
public class MeasureTimeouts extends Measure {

    protected int timeoutsCount = 0;

    /**
     * @param timeoutsCount - количество таймаутов на интервале. Здесь собиарем все таймауты со всех потоков за все время эксперимента
     */
    synchronized public void addTimeouts(int timeoutsCount) {
        this.timeoutsCount += timeoutsCount;
    }

    public int averageTimeoutOnInterval(int insertCount, int onIntervalCount) {
        int average = (int)((onIntervalCount * timeoutsCount) / insertCount);
        return average;
    }
}

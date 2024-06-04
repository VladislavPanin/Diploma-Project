package com.sibertech.lib.db.measure;

public class MeasureInsTime extends Measure {

    /**
     * @param insertCount - обеще количество вставок в таблицу sales за все время всех потоков
     * @param onIntervalCount  - на пустой таблице, время одной вставки может оказаться меньше 1 микросекунды, то есть 0.
     *                           поэтому будем делать замер суммарного времени для onIntervalCount вставок (например 50мкс на 100 вставок).
     * @return среднее время вставки onIntervalCount строк
    */
    public long averageInsTimeOnInterval(int insertCount, int onIntervalCount) {
        int average = (int)((onIntervalCount * spentTime) / insertCount);
        return average;
    }
}

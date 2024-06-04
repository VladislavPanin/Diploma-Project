package com.sibertech.etalon_summ_srv.services;

import com.sibertech.lib.conf.Conf;
import com.sibertech.lib.conf.LogWriter;
import com.sibertech.lib.params.http_client.Point;
import java.io.IOException;

public class IntervalInsertPoints {

    int lines = -1; // 10 000, 20 000, 30 000, 40 000 ... - точки оси Х, количество вставленных строк в sales
    int receivedPointCount = 0; // количество собранных потоков, нужно собрать DB_INSERTER_THREADS_COUNT_AT_ALL штук
    int spentTimeMs = 0; // максимальное время на вставку для всех потоков на этом интервале

    public IntervalInsertPoints (int lines) {
        this.lines = lines;
    }

    public void add(Point point, LogWriter log_inserts) throws IOException {

        int newSpentTimeMs = point.getY();
        spentTimeMs = (spentTimeMs > newSpentTimeMs) ? spentTimeMs : newSpentTimeMs;

        receivedPointCount++;
        int full = Conf.inst().app().getDB_INSERTER_THREADS_COUNT_AT_ALL();
        
        if (receivedPointCount >= full)
        {
            int countOfAllLines = receivedPointCount * this.lines;
            // записать в лог
            log_inserts._log(countOfAllLines, spentTimeMs);
        }

        //String msg = String.format(" ------------- поток #%-2d  (INSERTED)    X=%d    Y=%d", point.getThread_num(), point.getX(), point.getY());
        //System.out.println(msg);
    }

}

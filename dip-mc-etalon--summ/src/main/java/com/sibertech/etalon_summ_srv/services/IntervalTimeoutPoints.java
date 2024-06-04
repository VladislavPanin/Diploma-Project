package com.sibertech.etalon_summ_srv.services;

import com.sibertech.lib.conf.Conf;
import com.sibertech.lib.conf.LogWriter;
import com.sibertech.lib.params.http_client.Point;
import java.io.IOException;

public class IntervalTimeoutPoints {

    int lines = -1; // 10 000, 20 000, 30 000, 40 000 ... - точки оси Х, количество вставленных строк в sales
    int receivedPointCount = 0; // количество собранных потоков, нужно собрать DB_INSERTER_THREADS_COUNT_AT_ALL штук
    int occuredTimeouts = 0; // здесь суммируем таймауты всх потоков на интервале замера

    public IntervalTimeoutPoints (int lines) {
        this.lines = lines;
    }

    public void add(Point point, LogWriter log_timeouts) throws IOException {

        occuredTimeouts += point.getY();

        receivedPointCount++;
        int full = Conf.inst().app().getDB_INSERTER_THREADS_COUNT_AT_ALL();
        
        if (receivedPointCount >= full)
        {
            int countOfAllLines = receivedPointCount * this.lines;
            // записать в лог
            log_timeouts._log(countOfAllLines, occuredTimeouts);
        }

        //String msg = String.format(" ------------- поток #%-2d  (TIMEOUTS)    X=%d    Y=%d", point.getThread_num(), point.getX(), point.getY());
        //System.out.println(msg);
    }

}

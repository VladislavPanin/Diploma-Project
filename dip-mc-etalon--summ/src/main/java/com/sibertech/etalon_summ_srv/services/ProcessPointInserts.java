package com.sibertech.etalon_summ_srv.services;

import com.sibertech.lib.conf.LogFactory;
import com.sibertech.lib.conf.LogWriter;
import com.sibertech.lib.params.http_client.Point;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProcessPointInserts {

    public LogWriter log_inserts;
    protected boolean fisrtInsert = true;
    {
        try {
            log_inserts = LogFactory.get_for_dip_inserted_rows_summ(LogFactory.LOG_MISSION_ETAL);
        }
        catch (IOException ex) {
        }
    }

    protected Map<Integer, IntervalInsertPoints> mapConts= new HashMap <>();

    synchronized public void proc(Point point) throws IOException {
        if (fisrtInsert) {
            log_inserts.start();
            fisrtInsert = false;
        }
        IntervalInsertPoints  interval;
        int countOfInsertedLines = point.getX(); // точка (число вставленных строк в sales) для одного потока

        if (mapConts.containsKey(countOfInsertedLines)) {
            interval = mapConts.get (countOfInsertedLines);
        }
        else {
            interval = new IntervalInsertPoints(countOfInsertedLines);
            mapConts.put(countOfInsertedLines, interval);
        }
        interval.add(point, log_inserts);
    }
}

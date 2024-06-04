package com.sibertech.etalon_summ_srv.services;

import com.sibertech.lib.conf.LogFactory;
import com.sibertech.lib.conf.LogWriter;
import com.sibertech.lib.params.http_client.Point;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProcessPointTimeouts {

    public LogWriter log_timeouts;
    protected boolean fisrtInsert = true;
    {
        try {
            log_timeouts = LogFactory.get_for_dip_timeout_count_summ(LogFactory.LOG_MISSION_ETAL);
        }
        catch (IOException ex) {
        }
    }

    protected Map<Integer, IntervalTimeoutPoints> mapConts= new HashMap <>();

    synchronized public void proc(Point point) throws IOException {

        if (fisrtInsert) {
            log_timeouts.start();
            fisrtInsert = false;
        }

        IntervalTimeoutPoints  interval;
        int countOfInsertedLines = point.getX();

        if (mapConts.containsKey(countOfInsertedLines)) {
            interval = mapConts.get (countOfInsertedLines);
        }
        else {
            interval = new IntervalTimeoutPoints(countOfInsertedLines);
            mapConts.put(countOfInsertedLines, interval);
        }
        interval.add(point, log_timeouts);
    }

}

package com.sibertech.lib.db.measure;

public class Measure {

    protected long startTime  = -1L;
    protected long finishTime = -1L;
    protected long spentTime  = -1L;

    public long start() {
        startTime = System.currentTimeMillis();
        return startTime;
    }

    public long finish() {
        finishTime = System.currentTimeMillis();
        spentTime = finishTime - startTime;
        return finishTime;
    }

    public long spentTimeMsec() {
        return spentTime;
    }

    public void reset() {
        startTime  = -1L;
        finishTime = -1L;
        spentTime  = -1L;
    }
}

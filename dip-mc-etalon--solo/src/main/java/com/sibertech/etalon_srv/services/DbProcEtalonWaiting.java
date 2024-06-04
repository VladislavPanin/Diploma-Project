package com.sibertech.etalon_srv.services;

import com.sibertech.etalon_srv.http.sender.ToSummSender;
import com.sibertech.lib.conf.ConfApp;
import com.sibertech.lib.db.measure.DipTimer;
import com.sibertech.lib.db.measure.MeasureTimeouts;
import com.sibertech.lib.params.http_client.MyObjResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbProcEtalonWaiting implements Runnable {

    protected ArrayList<ScheduledFuture<String>> arrFutures;
    protected Logger logback = LoggerFactory.getLogger(DbProcEtalonWaiting.class);
    protected DipTimer summaryTimer = null;
    protected MeasureTimeouts measureTimeouts = null;

    public void init (
            ArrayList<ScheduledFuture<String>> arrFutures,
            DipTimer summaryTimer,
            MeasureTimeouts measureTimeouts) throws IOException {

        this.measureTimeouts = measureTimeouts;
        this.summaryTimer = summaryTimer;
        this.arrFutures = arrFutures;
    }

    @Override
    public void run() {

        ArrayList<String> arrMsg = new ArrayList<>();
        String msg;
        for (Future<String> fut : arrFutures)
        {
           try {
               msg = fut.get();   // здесь текущий поток будет висеть, пока не получит результаты работы запущеннывх потоков (то есть пока потоки не завершатся)
               arrMsg.add(msg);        // собираем все отчеты выполнения в один контейнер - выведем все сразу, после того, как все потоки (которых ожидаем на предыдущей строке) выполнятся
           }
           catch (InterruptedException | ExecutionException ex) {
               msg = ex.getMessage();
               logback.error(msg);
               arrMsg.add(msg);
           }
        }
        summaryTimer.finish();

        String msgCumulative = "\n";
        MyObjResponse msgResp = new MyObjResponse();
        msgResp.setSuccess(false);

        logback.info(String.format("%s", ConfApp.LOG_SPLIT));
        logback.info(String.format("%s Завершены все потоки вставки ThreadRunner: schedule_threads_etalon(), получены сообщения:", ConfApp.LOG_PEFIX));
        for (String mesg: arrMsg){
            logback.info(String.format("%s %s", ConfApp.LOG_PEFIX, mesg));
            msgCumulative += mesg + "\n";
            msgResp.setSuccess(true);
        }

        logback.info(String.format("%s", ConfApp.LOG_SPLIT));
        // -------------------------------------------------------
        int spentTimeMillisec = summaryTimer.getSpentTimeMillisec();
        int timeoutsCount = measureTimeouts.getTimeoutsCount();

        msgResp.setMsg(msgCumulative);
        msgResp.setNum(arrMsg.size());
        msgResp.setSpentTimeMillisec(spentTimeMillisec);
        msgResp.setTimeoutsCount(timeoutsCount);

        try {
            ToSummSender.sendOver(msgResp);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(DbProcEtalonWaiting.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
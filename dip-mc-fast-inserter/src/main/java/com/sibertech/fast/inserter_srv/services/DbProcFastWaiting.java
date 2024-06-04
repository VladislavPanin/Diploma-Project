package com.sibertech.fast.inserter_srv.services;

import com.sibertech.lib.conf.ConfApp;
import com.sibertech.lib.db.DB;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbProcFastWaiting extends DB implements Runnable {

    protected ArrayList<ScheduledFuture<String>> arrFutures;
    protected Logger logback = LoggerFactory.getLogger(DbProcFastWaiting.class);

    public void init (ArrayList<ScheduledFuture<String>> arrFutures) throws IOException {
        this.arrFutures = arrFutures;
    }

    @Override
    public void run() {
        
        ArrayList<String> arrMsg = new ArrayList<>();
        String msg;
        for (Future<String> fut : arrFutures) 
        {
           try 
           {
               msg = fut.get();   // здесь текущий поток будет висеть, пока не получит результаты работы запущеннывх потоков (то есть пока потоки не завершатся)
               arrMsg.add(msg);        // собираем все отчеты выполнения в один контейнер - выведем все сразу, после того, как все потоки (которых ожидаем на предыдущей строке) выполнятся
           } 
           catch (InterruptedException | ExecutionException ex) 
           {
               msg = ex.getMessage();
               logback.error(msg);
               arrMsg.add(msg);
           }
        }

        logback.info(String.format("%s", ConfApp.LOG_SPLIT));
        logback.info(String.format("%s Завершены все потоки вставки ThreadRunner: schedule_threads_fast(), получены сообщения:", ConfApp.LOG_PEFIX));
        for (String mesg: arrMsg){
            logback.info(String.format("%s %s", ConfApp.LOG_PEFIX, mesg));
        }
        logback.info(String.format("%s", ConfApp.LOG_SPLIT)); 
    }
}
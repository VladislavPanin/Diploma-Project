package com.sibertech.etalon_srv.services;

import com.sibertech.lib.conf.Conf;
import com.sibertech.lib.conf.ConfApp;
import com.sibertech.lib.db.measure.DipTimer;
import com.sibertech.lib.db.measure.MeasureTimeouts;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadRunner {

    protected MeasureTimeouts measureTimeouts = new MeasureTimeouts();

    protected ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(12);
    protected ExecutorService          fixedThreadPool = Executors.newFixedThreadPool(1);

    protected Logger logback = LoggerFactory.getLogger(ThreadRunner.class);

    public void schedule_threads_etalon(int threadCount, long delay, DipTimer summaryTimer) throws IOException, InterruptedException, ExecutionException {

        ArrayList<ScheduledFuture<String>> arrFutures = new ArrayList<>();
        for (int iTreadNum=0; iTreadNum<threadCount; iTreadNum++)
        {
            DbProcEtalon proc = new DbProcEtalon ();       // создаем экземпляр специализированного класса-обработчика, который будет запущен в новом потоке
            proc.init(iTreadNum, measureTimeouts);                          // будет создан DbProcEtalon.insertedRowsLogger - логгер потока

            ScheduledFuture<String> future = this.scheduledThreadPool.schedule(proc, delay, TimeUnit.SECONDS); // получаем объект future, который будет узнает, когда закончится поток, от которого его получили
            arrFutures.add(future);                       // складываем в один контейнер все такие объекты future - удобно
            logback.info(String.format("%s ThreadRunner: Запланирован запуск потока вставки #%d, БД %s", ConfApp.LOG_PEFIX, iTreadNum, Conf.inst().app().getDbName()));
        }
        runWaitingThreads (arrFutures, summaryTimer, measureTimeouts);
    }

     public void runWaitingThreads (
             ArrayList<ScheduledFuture<String>> arrFutures,
             DipTimer summaryTimer,
             MeasureTimeouts measureTimeouts) throws IOException {

        DbProcEtalonWaiting waitingThread = new DbProcEtalonWaiting();
        waitingThread.init (arrFutures, summaryTimer, measureTimeouts);
        fixedThreadPool.submit(waitingThread);
    }
}

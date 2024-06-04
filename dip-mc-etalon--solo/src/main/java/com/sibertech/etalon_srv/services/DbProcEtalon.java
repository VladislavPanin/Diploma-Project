package com.sibertech.etalon_srv.services;

import com.sibertech.lib.conf.Conf;
import com.sibertech.lib.conf.ConfApp;
import com.sibertech.lib.conf.ConfDb;
import com.sibertech.lib.db.DB;
import com.sibertech.lib.db.measure.MeasureTimeouts;
import java.io.IOException;
import java.util.concurrent.Callable;

public class DbProcEtalon extends DB implements Callable {

    protected int       thread_num = -1;
    protected String    thread_result = "не запущен, не отработал";

    MeasureTimeouts measureTimeouts = null;

    public void init (int iTreadNum, MeasureTimeouts measureTimeouts) throws IOException {
        this.thread_num = iTreadNum;
        this.measureTimeouts = measureTimeouts;
    }

    @Override
    public String call() throws Exception {

        ConfApp app = Conf.inst().app();
        //сделано для того, чтобы исключить одновременный старт всех 10 потоков. Заметно это будет только при выставленном lock_timeout - на пустой таблице задержка первой вставки будет 7 мин
        //Можно закоментарить эту строку и посмотреть что получится (если будет время)
        Thread.sleep(thread_num * 10);

        thread_result = "запущен, работает";
        String connURL = ConfDb.BASE_DB_URL + app.getDbName();
        this.init(connURL, ConfDb.PG_DB_USER, ConfDb.PG_DB_PWD);

            logbackDB.warn(String.format("%s (ETALON) Запущен  поток #%2d, вставка в таблицу -- %s --, БД  -- %s -- (%,10d записей)",
                    ConfApp.LOG_PEFIX,
                    thread_num,
                    app.getSalesTableName(),
                    app.getDbName(),
                    app.getBreakDip_afterCount()
            ));

                InsEtalonSales insSales = new InsEtalonSales(measureTimeouts);

                insSales.insert_etalon (thread_num, Conf.inst().data(), conn);
                thread_result = String.format("(ETALON) Завершен поток вставки #%d. Вставлено %,7d строк в таблицу %s", thread_num, insSales.getCountOfInsertedLines(), app.getSalesTableName());

            logbackDB.warn(String.format("%s %s", ConfApp.LOG_PEFIX, thread_result));

        this.close();
        return thread_result;
    }
}
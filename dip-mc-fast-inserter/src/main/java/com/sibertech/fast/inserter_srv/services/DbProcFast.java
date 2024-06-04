package com.sibertech.fast.inserter_srv.services;

import com.sibertech.lib.conf.Conf;
import com.sibertech.lib.conf.ConfApp;
import com.sibertech.lib.conf.ConfDb;
import com.sibertech.lib.db.DB;
import java.io.IOException;
import java.util.concurrent.Callable;

public class DbProcFast extends DB implements Callable {

    protected int       thread_num = -1;
    protected String    thread_result = "не запущен, не отработал";

    public void init (int iTreadNum) throws IOException {
        this.thread_num = iTreadNum;
    }

    @Override
    public String call() throws Exception {

        ConfApp app = Conf.inst().app();

        thread_result = "запущен, работает";
        String connURL = ConfDb.BASE_DB_URL + app.getDbName();
        this.init(connURL, ConfDb.PG_DB_USER, ConfDb.PG_DB_PWD);

            logbackDB.warn(String.format("%s (%s) Запущен  поток #%2d, вставка в таблицу -- %s --, БД  -- %s -- (%,10d записей)",
                    ConfApp.LOG_PEFIX,
                    Conf.mcServName(),
                    thread_num,
                    app.getSalesTableName(),
                    app.getDbName(),
                    app.getBreakDip_afterCount()
            ));

                SendFastSales sendSales = new SendFastSales();

                sendSales.insert_fast (thread_num, Conf.inst().data(), conn);
                int countOfSendLines = sendSales.getCountOfSendBasked() * ConfDb.COUNT_OF_PRODUCTS_IN_BASKET;
                thread_result = String.format("(%s) Завершен поток вставки #%d. Вставлено %,7d строк в таблицу %s",Conf.mcServName(), thread_num, countOfSendLines, app.getSalesTableName());

            logbackDB.warn(String.format("%s %s", ConfApp.LOG_PEFIX, thread_result));

        this.close();
        return thread_result;
    }
}
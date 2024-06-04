package com.sibertech.fast.collector_srv.services;

import com.sibertech.lib.conf.Conf;
import com.sibertech.lib.conf.ConfApp;
import com.sibertech.lib.conf.ConfDb;
import com.sibertech.lib.conf.LogFactory;
import com.sibertech.lib.conf.LogWriter;
import com.sibertech.lib.db.DB;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsFastCollectorSales extends DB implements Callable {

    protected Conf conf = Conf.inst();
    protected static Logger logback = LoggerFactory.getLogger(InsFastCollectorSales.class);

    protected ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue();
    protected int countToInsert = conf.app().getBreakDip_afterCount()*conf.app().getDB_INSERTER_THREADS_COUNT_AT_ALL();

    protected long startTimeInterval_forInsert = 0L; // замер времени для построения графика при вставке в таблицы продаж (sales и dictionary)
    protected long endTimeInterval_forInsert = 0L;   // замер времени для построения графика при вставке в таблицы продаж (sales и dictionary)

    protected LogWriter insertsLogger = null;       // логгер замеров для графиков (выводит в файл и в консоль одновременно)
    protected int countOfInsertedBasket = 0;
    // =============================================================================
    protected String sqlOper = "INSERT INTO buffer_in(obj_record) VALUES(?)";

    public    void incBasketCount () {
        countOfInsertedBasket++;
    }
    public int getCountOfInsertedLines () {
        return countOfInsertedBasket * ConfDb.COUNT_OF_PIECES_IN_BASKET;
    }
    // =============================================================================
    @Override
    public Object call() throws Exception {

        insertsLogger = LogFactory.get_for_dip_inserted_rows_collector();

        ConfApp app = Conf.inst().app();
        String thread_result = "запущен, работает";

        String connURL = ConfDb.BASE_DB_URL + app.getDbName();
        this.init(connURL, ConfDb.PG_DB_USER, ConfDb.PG_DB_PWD);

            logback.warn(String.format("%s (%s) Запущен  поток вставки в таблицу -- buffer_in --, БД  -- %s -- (%,10d записей)",
                    ConfApp.LOG_PEFIX,
                    Conf.mcServName(),
                    app.getDbName(),
                    this.countToInsert
            ));

            try {
                    do
                    {
                        insert ();
                    }
                    while(this.countToInsert > this.getCountOfInsertedLines());
            }
            catch(Exception exc)
            {
                String msg = exc.getMessage();
                logback.error(String.format("%s InsFastCollectorSales.call(): %s", ConfApp.LOG_PEFIX_EXCEPT, msg));
            }

            thread_result = String.format("(%s) Завершен поток вставки в таблицу -- buffer_in -- на сервисе fast collector",Conf.mcServName());
            logback.warn(String.format("%s %s", ConfApp.LOG_PEFIX, thread_result));
            logback.warn(String.format("%s  =============== Новый поток вставки будет запущен при обновлении конфигурации =============== ", ConfApp.LOG_PEFIX));
            logback.warn(String.format("%s  ============================================================================================= ", ConfApp.LOG_PEFIX));

        this.close();
        return thread_result;
    }
    // =============================================================================
    // AtomicInteger ai = new AtomicInteger(0);
    public void add(String jBasket) {
        queue.add(jBasket);
        //int i = ai.incrementAndGet();
        //System.out.println("ai.incrementAndGet()=" + i);
    }

    String jBasket;
    protected void insert() throws SQLException, IOException {
        jBasket = (String)queue. poll();

        if (null != jBasket) {
            process(jBasket);
            return;
        }
        try {Thread.sleep(200);}
        catch (Exception dummy)
        {
            String msg = dummy.getMessage();
            int i = 0;
        }
    }

    private boolean firstTime = true;
    protected void process(String jBask) throws SQLException, IOException {

        if (firstTime) {
            insertsLogger.start();
            startTimeInterval_forInsert = System.currentTimeMillis();
            firstTime = false;
        }

        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sqlOper);)
        {
            preparedStatement.setString(1,  jBask);
            preparedStatement.execute();
            this.incBasketCount();
        }

        int countOfInsertedLines = this.getCountOfInsertedLines ();
        if (countOfInsertedLines % conf.app().getInsertStepCounter()== 0 && countOfInsertedLines != 0) {
                // количество продуктов в корзине должно быть кратно 10 - иначе сюда никогда не попадем

                endTimeInterval_forInsert = System.currentTimeMillis();
                int intervalOnInserts = (int)(endTimeInterval_forInsert - startTimeInterval_forInsert);
                insertsLogger. _log(countOfInsertedLines, intervalOnInserts); // вывод графика зависимости времени вставки строки от количества строк в таблице

                startTimeInterval_forInsert = endTimeInterval_forInsert;
            }
    }
}

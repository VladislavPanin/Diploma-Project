package com.sibertech.lib.Gen.db.ins.core;

import com.sibertech.lib.conf.DSet;
import com.sibertech.lib.conf.ConfApp;
import com.sibertech.lib.conf.Conf;
import com.sibertech.lib.conf.LogWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Ins {

    protected Conf conf = Conf.inst();

    protected String loggedTable = "none"; // имя таблицы куда вставляем - для вывода информации в консоль. Устанавливаем перед началом вставки в таблицу

    protected long startTime = 0L; // замер времени на всю вставку  в таблицу (для любых таблиц)
    protected long endTime = 0L;   // замер времени на всю вставку  в таблицу (для любых таблиц)

    protected long startTimeInterval_forInsert = 0L; // замер времени на для построения графика при вставке в таблицы продаж (sales и dictionary)
    protected long endTimeInterval_forInsert = 0L;   // замер времени на для построения графика при вставке в таблицы продаж (sales и dictionary)

    protected Logger logback = LoggerFactory.getLogger(Ins.class);

    // подсчет вставленных строк в таблицу: int countOfInsertedLines  и void incLineCount ()
    protected int countOfInsertedLines = 0;
    public    void incLineCount () {
        countOfInsertedLines++;
    }
    public int getCountOfInsertedLines () {
        return countOfInsertedLines;
    }

    protected LogWriter insertedRowsLogger = null;       // логгер замеров для графиков (выводит в файл и в консоль одновременно)
    protected String    serviceThreadInfo = null;

    // ======================================================================= //
    abstract protected void insert_internal (DSet dSet, Connection conn)  throws SQLException;

    // реализация для генератора dip.gen
    public void insert (LogWriter log, DSet dSet, Connection conn)  throws SQLException, IOException {

        this. insertedRowsLogger = log;
        this. serviceThreadInfo = Conf.mcServName();

        printStart (loggedTable, conf.app().getDbName());
            insert_internal (dSet, conn);
        printEnd   (loggedTable, conf.app().getDbName());
    }
    public void insert (int thread_num, LogWriter log, DSet dSet, Connection conn)  throws SQLException, IOException {

        this. insertedRowsLogger = log;
        this. serviceThreadInfo = Conf.mcServName() + ", thread num#" + thread_num;

        printStart (loggedTable, conf.app().getDbName());
            insert_internal (dSet, conn);
        printEnd   (loggedTable, conf.app().getDbName());
    }
    // ======================================================================= //

    public void printStart (String table, String db)
    {
        startTime = System.currentTimeMillis();
        logback.warn(String.format("%s :::::::::: (%s) Начата    вставка в таблицу -- %30s :: %-10s --", ConfApp.LOG_PEFIX, serviceThreadInfo, db, table));
    }

    public void printEnd (String table, String db)
    {
        endTime = System.currentTimeMillis();
        long delta = endTime - startTime;
        logback.warn(String.format("%s :::::::::: (%s) Закончена вставка в таблицу -- %30s :: %-10s --. Количество вставленных строк %,d. Время вставки %d миллисекунд (или %d секунд или  %d минут)", ConfApp.LOG_PEFIX, serviceThreadInfo, db, table, countOfInsertedLines, delta, delta/1000, delta/(60*1000)));
    }

    public String getLoggedTable() {// имя таблицы куда вставляем - для вывода информации в консоль. Устанавливаем перед началом вставки в таблицу
        return loggedTable;
    }

    public void setLoggedTable(String loggedTable) {// имя таблицы куда вставляем - для вывода информации в консоль. Устанавливаем перед началом вставки в таблицу
        this.loggedTable = loggedTable;
    }
}

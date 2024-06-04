package com.sibertech.lib.db;

import com.sibertech.lib.conf.ConfDb;
import com.sibertech.lib.conf.ConfApp;
import com.sibertech.lib.conf.Conf;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbRemoveExceedFromSales extends DB {

    protected Conf lib = Conf.inst();

    protected Logger logback = LoggerFactory.getLogger(DbRemoveExceedFromSales.class);
    protected ArrayList<PairTableWithLastID> dictionaryTables = new ArrayList<>(){{
        add (new PairTableWithLastID("order_numbers_dictionary__01",   100000));
        add (new PairTableWithLastID("order_numbers_dictionary__02",   200000));
        add (new PairTableWithLastID("order_numbers_dictionary__03",   300000));
        add (new PairTableWithLastID("order_numbers_dictionary__04",   400000));
        add (new PairTableWithLastID("order_numbers_dictionary__05",   500000));
        add (new PairTableWithLastID("order_numbers_dictionary__06",   600000));
        add (new PairTableWithLastID("order_numbers_dictionary__07",   700000));
        add (new PairTableWithLastID("order_numbers_dictionary__08",   800000));
        add (new PairTableWithLastID("order_numbers_dictionary__09",   900000));
        add (new PairTableWithLastID("order_numbers_dictionary__10",  1000000));
        add (new PairTableWithLastID("order_numbers_dictionary__20",  2000000));
        add (new PairTableWithLastID("order_numbers_dictionary__30",  3000000));
        add (new PairTableWithLastID("order_numbers_dictionary__40",  4000000));
        add (new PairTableWithLastID("order_numbers_dictionary__50",  5000000));
        add (new PairTableWithLastID("order_numbers_dictionary__60",  6000000));
        add (new PairTableWithLastID("order_numbers_dictionary__70",  7000000));
        add (new PairTableWithLastID("order_numbers_dictionary__80",  8000000));
        add (new PairTableWithLastID("order_numbers_dictionary__90",  9000000));
        add (new PairTableWithLastID("order_numbers_dictionary_100", 10000000));
    }};

    protected ArrayList<PairTableWithLastID> salesTables = new ArrayList<>(){{
        add (new PairTableWithLastID("sales__01",   1000000));
        add (new PairTableWithLastID("sales__02",   2000000));
        add (new PairTableWithLastID("sales__03",   3000000));
        add (new PairTableWithLastID("sales__04",   4000000));
        add (new PairTableWithLastID("sales__05",   5000000));
        add (new PairTableWithLastID("sales__06",   6000000));
        add (new PairTableWithLastID("sales__07",   7000000));
        add (new PairTableWithLastID("sales__08",   8000000));
        add (new PairTableWithLastID("sales__09",   9000000));
        add (new PairTableWithLastID("sales__10",  10000000));
        add (new PairTableWithLastID("sales__20",  20000000));
        add (new PairTableWithLastID("sales__30",  30000000));
        add (new PairTableWithLastID("sales__40",  40000000));
        add (new PairTableWithLastID("sales__50",  50000000));
        add (new PairTableWithLastID("sales__60",  60000000));
        add (new PairTableWithLastID("sales__70",  70000000));
        add (new PairTableWithLastID("sales__80",  80000000));
        add (new PairTableWithLastID("sales__90",  90000000));
        add (new PairTableWithLastID("sales_100", 100000000));
    }};

    public String removeExceedFromSales () throws SQLException, ClassNotFoundException {
        String ret;
        logback.warn(String.format("%s :::::::::: (БД %s) Удаление излишка строк(свыше указанного в имени) из таблиц sales и dictionary", ConfApp.LOG_PEFIX, lib.app().getDbName()));

        try {
            String connURL = ConfDb.BASE_DB_URL + lib.app().getDbName();
            this.init(connURL, ConfDb.PG_DB_USER, ConfDb.PG_DB_PWD);

                for(PairTableWithLastID pair: salesTables) {
                    String oper = String.format("DELETE FROM %s WHERE id > %d", pair.tableName, pair.lastID);

                    logback.warn(String.format("%s (БД %s) %s ...", ConfApp.LOG_PEFIX, lib.app().getDbName(), oper));
                    try (PreparedStatement preparedStatement = conn.prepareStatement(oper);) {
                        preparedStatement.execute();
                    }
                }
                for(PairTableWithLastID pair: dictionaryTables) {
                    String oper = String.format("DELETE FROM %s WHERE order_number > %d", pair.tableName, pair.lastID);

                    logback.warn(String.format("%s (БД %s) %s ...", ConfApp.LOG_PEFIX, lib.app().getDbName(), oper));
                    try (PreparedStatement preparedStatement = conn.prepareStatement(oper);) {
                        preparedStatement.execute();
                    }
                }
                
                this.close();
                ret = "Удаление данных завершилось успешно";

        }
        catch (Exception exc)
        {
            String msg = exc.getMessage();
            ret = "Удаление данных завершилось исключением.\n Сообщение: " + msg;
        }

        logback.warn(String.format("%s :::::::::: Завершено удаление строк(свыше указанного в имени) из таблиц sales и dictionary, бд %s с результатом %s", ConfApp.LOG_PEFIX, lib.app().getDbName(), ret));
        return ret;
    }
}

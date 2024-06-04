package com.sibertech.lib.db;

import com.sibertech.lib.conf.ConfDb;
import com.sibertech.lib.conf.ConfApp;
import com.sibertech.lib.conf.Conf;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbClearSales extends DB {

    protected Conf lib = Conf.inst();

    protected Logger logback = LoggerFactory.getLogger(DbClearSales.class);

    public String clearSales () throws SQLException, ClassNotFoundException {
        String ret;
        logback.warn(String.format("%s :::::::::: (БД %s) Удаление содержимого таблиц sales и dictionary", ConfApp.LOG_PEFIX, lib.app().getDbName()));

        try {
            String connURL = ConfDb.BASE_DB_URL + lib.app().getDbName();
            this.init(connURL, ConfDb.PG_DB_USER, ConfDb.PG_DB_PWD);

                
                String oper = "DELETE FROM sales";

                logback.warn(String.format("%s (БД %s) %s ...", ConfApp.LOG_PEFIX, lib.app().getDbName(), oper));
                try (PreparedStatement preparedStatement = conn.prepareStatement(oper);) {
                    preparedStatement.execute();
                }

                oper = "DELETE FROM order_numbers_dictionary";

                logback.warn(String.format("%s (БД %s) %s ...", ConfApp.LOG_PEFIX, lib.app().getDbName(), oper));
                try (PreparedStatement preparedStatement = conn.prepareStatement(oper);) {
                    preparedStatement.execute();
                }
                
                this.close();
                ret = "Удаление содержимого таблиц sales и dictionary завершилось успешно";

        }
        catch (Exception exc)
        {
            String msg = exc.getMessage();
            ret = "Удаление содержимого таблиц sales и dictionary завершилось исключением.\n Сообщение: " + msg;
        }

        return ret;
    }
}

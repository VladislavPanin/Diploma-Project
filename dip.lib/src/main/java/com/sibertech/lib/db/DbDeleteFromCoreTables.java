package com.sibertech.lib.db;

import com.sibertech.lib.conf.ConfDb;
import com.sibertech.lib.conf.ConfApp;
import com.sibertech.lib.conf.Conf;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbDeleteFromCoreTables extends DB {

    protected Conf lib = Conf.inst();

    protected Logger logback = LoggerFactory.getLogger(DbDeleteFromCoreTables.class);
    protected ArrayList<String> tables = new ArrayList<>(){{
      add ("buffer_in");
      add ("buffer_quality");
      add ("buffer_processing");
      add ("sales");
      add ("order_numbers_dictionary");
      add ("terminals");
      add ("markets");
      add ("loyalities");
      add ("loyality_params");
      add ("products");
      add ("product_categories");
      add ("clients");
      add ("regions");
    }};

    protected String sTableList = String.join(", ", tables);

    public String deleteFromCoreTables () throws SQLException, ClassNotFoundException {
        String ret;
        logback.warn(String.format("%s :::::::::: (%s) (БД %s) Попытка удаления содержимого таблиц: %s", ConfApp.LOG_PEFIX, Conf.mcServName(), lib.app().getDbName(), sTableList));

        try {
            String connURL = ConfDb.BASE_DB_URL + lib.app().getDbName();
            this.init(connURL, ConfDb.PG_DB_USER, ConfDb.PG_DB_PWD);

                for(String tableName: tables) {
                    String oper = String.format("DELETE FROM %s", tableName);

                    logback.warn(String.format("%s (%s) (БД %s) %s ...", ConfApp.LOG_PEFIX, Conf.mcServName(), lib.app().getDbName(), oper));
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

        logback.warn(String.format("%s :::::::::: (%s) Завершено удаление данных из таблиц бд %s с результатом: %s", ConfApp.LOG_PEFIX, Conf.mcServName(), lib.app().getDbName(), ret));
        return ret;
    }
}

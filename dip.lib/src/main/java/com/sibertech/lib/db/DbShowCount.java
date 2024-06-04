package com.sibertech.lib.db;

import com.sibertech.lib.conf.ConfDb;
import com.sibertech.lib.conf.ConfApp;
import com.sibertech.lib.conf.Conf;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbShowCount extends DB {

    protected Conf lib = Conf.inst();
    protected Logger logback = LoggerFactory.getLogger(DbShowCount.class);
    protected String retHTML = "";

    public String showCountSales () throws SQLException, ClassNotFoundException {

        retHTML = "";
            logback.warn(String.format("%s :::::::::: (%s) (БД %s) Запрос количества строк в таблицах sales и dictionary", ConfApp.LOG_PEFIX, Conf.mcServName(), lib.app().getDbName()));
                showCount (salesTables);
            logback.warn(String.format("%s :::::::::: (%s) (БД %s) Запрос количества строк завершен", ConfApp.LOG_PEFIX,Conf.mcServName(), lib.app().getDbName()));
        return retHTML;
    }

    public String showCountCore () throws SQLException, ClassNotFoundException {

        retHTML = "";
            logback.warn(String.format("%s :::::::::: (%s) (БД %s) Запрос количества строк в базовых таблицах", ConfApp.LOG_PEFIX, Conf.mcServName(), lib.app().getDbName()));
                showCount (coreTables);
            logback.warn(String.format("%s :::::::::: (%s) (БД %s) Запрос количества строк завершен", ConfApp.LOG_PEFIX, Conf.mcServName(), lib.app().getDbName()));
        return retHTML;
    }

    protected void showCount (ArrayList<String> tables) throws SQLException, ClassNotFoundException {

        String connURL = ConfDb.BASE_DB_URL + lib.app().getDbName();
        this.init(connURL, ConfDb.PG_DB_USER, ConfDb.PG_DB_PWD);

            for(String tableName: tables)
            {
                String sqlOper = String.format("SELECT count(*) FROM %s", tableName);
                this.select(sqlOper, tableName);
            }
        this.close();
    }
    
     public String showCountAll () throws SQLException, ClassNotFoundException {
         
        String connURL = ConfDb.BASE_DB_URL + lib.app().getDbName();
        this.init(connURL, ConfDb.PG_DB_USER, ConfDb.PG_DB_PWD);

        retHTML = "<table>";
            logback.warn(String.format("%s :::::::::: (%s) (БД %s) Запрос количества строк таблиц", ConfApp.LOG_PEFIX, Conf.mcServName(), lib.app().getDbName()));
            
                for(String tableName: salesTables)
                {
                    String sqlOper = String.format("SELECT count(*) FROM %s", tableName);
                    this.select(sqlOper, tableName);
                }
                for(String tableName: coreTables)
                {
                    String sqlOper = String.format("SELECT count(*) FROM %s", tableName);
                    this.select(sqlOper, tableName);
                }
            logback.warn(String.format("%s :::::::::: (%s) (БД %s) Запрос количества строк завершен", ConfApp.LOG_PEFIX, Conf.mcServName(), lib.app().getDbName()));
            retHTML += "</table>";            
        
        this.close();
        return retHTML;
    }

    protected void select (String sqlOper, String  tableName) throws SQLException, ClassNotFoundException {

            try(PreparedStatement preparedStatement = this.conn.prepareStatement(sqlOper);)
            {
                    int count = -1;
                    try(ResultSet resultSet = preparedStatement.executeQuery();)
                    {
                        if (resultSet.next())
                        {
                            count = resultSet.getInt(1);
                        }
                        String line = String.format("%s %-20s \t %,d", ConfApp.LOG_PEFIX, tableName, count);
                        logback.warn(line);
                        
                        retHTML += String.format("<tr><td>%s</td><td>%d</td></tr>", tableName, count);
                    }
                }
    }

    protected ArrayList<String> salesTables = new ArrayList<>(){{
        add ("sales"); /*
        add ("sales__01");
        add ("sales__02");
        add ("sales__03");
        add ("sales__04");
        add ("sales__05");
        add ("sales__06");
        add ("sales__07");
        add ("sales__08");
        add ("sales__09");
        add ("sales__10");
        add ("sales__20");
        add ("sales__30");
        add ("sales__40");
        add ("sales__50");
        add ("sales__60");
        add ("sales__70");
        add ("sales__80");
        add ("sales__90");
        add ("sales_100"); */
        add ("order_numbers_dictionary"); /*
        add ("order_numbers_dictionary__01");
        add ("order_numbers_dictionary__02");
        add ("order_numbers_dictionary__03");
        add ("order_numbers_dictionary__04");
        add ("order_numbers_dictionary__05");
        add ("order_numbers_dictionary__06");
        add ("order_numbers_dictionary__07");
        add ("order_numbers_dictionary__08");
        add ("order_numbers_dictionary__09");
        add ("order_numbers_dictionary__10");
        add ("order_numbers_dictionary__20");
        add ("order_numbers_dictionary__30");
        add ("order_numbers_dictionary__40");
        add ("order_numbers_dictionary__50");
        add ("order_numbers_dictionary__60");
        add ("order_numbers_dictionary__70");
        add ("order_numbers_dictionary__80");
        add ("order_numbers_dictionary__90");
        add ("order_numbers_dictionary_100");*/
    }};

    protected ArrayList<String> coreTables = new ArrayList<>(){{ add ("buffer_in");
      add ("buffer_quality");
      add ("buffer_processing");
      add ("terminals");
      add ("markets");
      add ("loyalities");
      add ("loyality_params");
      add ("products");
      add ("product_categories");
      add ("clients");
      add ("regions");
    }};
}

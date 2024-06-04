package com.sibertech.lib.Gen.db.ins;

import com.sibertech.lib.Gen.db.ins.sales.InsSales;
import com.sibertech.lib.conf.Conf;
import com.sibertech.lib.db.DB;
import com.sibertech.lib.conf.DSet;
import com.sibertech.lib.conf.ConfDb;
import com.sibertech.lib.conf.ConfApp;
import com.sibertech.lib.conf.LogFactory;
import com.sibertech.lib.conf.LogWriter;
import java.io.IOException;
import java.sql.SQLException;

// реализация для генератора dip.gen
public class GenDbInsSales extends DB {

    protected Conf lib = Conf.inst();

    // реализация для генератора dip.gen
    public void insertSalesTables (DSet dSet) throws SQLException, ClassNotFoundException, IOException {

        LogWriter insertedRowsLogger = LogFactory.get_for_gen_sales();

        String connURL = ConfDb.BASE_DB_URL + lib.app().getDbName();
        this.init(connURL, ConfDb.PG_DB_USER, ConfDb.PG_DB_PWD);

        logbackDB.warn(ConfApp.LOG_SPLIT);
        logbackDB.warn(String.format("%s Начата    вставка даных таблиц продаж БД  -- %s --", ConfApp.LOG_PEFIX, lib.app().getDbName()));
        logbackDB.warn(String.format("%s (%,10d записей в таблицу sales)", ConfApp.LOG_PEFIX, lib.app().getBreakGen_afterCount()));

            InsSales insSales = new InsSales();
            insSales.setLoggedTable("sales");
            insSales.prepare (lib.app().getRootSalesTableName(), lib.app().getRootDictionaryTableName());

            insSales.insert (insertedRowsLogger, dSet, conn);

        logbackDB.warn(String.format("%s Закончена вставка даных таблиц продаж БД  -- %s --", ConfApp.LOG_PEFIX, lib.app().getDbName()));
        logbackDB.warn(ConfApp.LOG_SPLIT);

        this.close();
    }
}

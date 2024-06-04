package com.sibertech.lib.Gen.db.ins;

import com.sibertech.lib.Gen.db.ins.core.InsLoyality;
import com.sibertech.lib.Gen.db.ins.core.InsRegions;
import com.sibertech.lib.Gen.db.ins.core.InsLoyalityParams;
import com.sibertech.lib.Gen.db.ins.core.InsRegionMarkets;
import com.sibertech.lib.Gen.db.ins.core.InsProducts;
import com.sibertech.lib.Gen.db.ins.core.InsClients;
import com.sibertech.lib.Gen.db.ins.core.InsProductCategories;
import com.sibertech.lib.Gen.db.ins.core.InsRegionMarketTerminals;
import com.sibertech.lib.conf.DSet;
import com.sibertech.lib.conf.ConfDb;
import com.sibertech.lib.conf.ConfApp;
import com.sibertech.lib.conf.Conf;
import com.sibertech.lib.conf.LogFactory;
import com.sibertech.lib.conf.LogWriter;
import com.sibertech.lib.db.DB;
import java.io.IOException;
import java.sql.SQLException;

// реализация для генератора dip.gen
public class GenDbInsCore extends DB {

    protected Conf lib = Conf.inst();

    // реализация для генератора dip.gen
    public void insertCoreTables (DSet dSet) throws SQLException, ClassNotFoundException, IOException {

        LogWriter insertedRowsLogger = LogFactory.get_for_gen_core();

        String connURL = ConfDb.BASE_DB_URL + lib.app().getDbName();
        this.init(connURL, ConfDb.PG_DB_USER, ConfDb.PG_DB_PWD);

        logbackDB.warn(ConfApp.LOG_SPLIT);
        logbackDB.warn(String.format("%s (%s) Начата    вставка даных базовых таблиц БД  -- %s --", ConfApp.LOG_PEFIX, lib.mcServName(), lib.app().getDbName()));

            InsRegions insRegions = new InsRegions();
            insRegions.setLoggedTable("regions");
            insRegions.insert (insertedRowsLogger, dSet, conn);

            InsRegionMarkets insRegionMarkets = new InsRegionMarkets();
            insRegionMarkets.setLoggedTable("markets");
            insRegionMarkets.insert (insertedRowsLogger, dSet, conn);

            InsRegionMarketTerminals insRegionMarketTerminals = new InsRegionMarketTerminals();
            insRegionMarketTerminals.setLoggedTable("terminals");
            insRegionMarketTerminals.insert (insertedRowsLogger, dSet, conn);
            // ----------------------------------------------------------------

            InsClients insClients = new InsClients();
            insClients.setLoggedTable("clients");
            insClients.insert (insertedRowsLogger, dSet, conn);
            // ----------------------------------------------------------------

            InsProductCategories insProductCategories = new InsProductCategories();
            insProductCategories.setLoggedTable("product_categories");
            insProductCategories.insert (insertedRowsLogger, dSet, conn);

            InsProducts insProducts = new InsProducts();
            insProducts.setLoggedTable("products");
            insProducts.insert (insertedRowsLogger, dSet, conn);
            // ----------------------------------------------------------------

            InsLoyalityParams insLoyalityParams = new InsLoyalityParams();
            insLoyalityParams.setLoggedTable("loyality_params");
            insLoyalityParams.insert (insertedRowsLogger, dSet, conn);

            InsLoyality insLoyality = new InsLoyality();
            insLoyality.setLoggedTable("loyalities");
            insLoyality.insert (insertedRowsLogger, dSet, conn);
            // ----------------------------------------------------------------

        logbackDB.warn(String.format("%s (%s) Закончена вставка даных базовых таблиц БД  -- %s --", ConfApp.LOG_PEFIX, lib.mcServName(), lib.app().getDbName()));
        logbackDB.warn(ConfApp.LOG_SPLIT);

        this.close();
    }
}

package com.sibertech.lib.Gen;

import com.sibertech.lib.DTO.Region;
import com.sibertech.lib.conf.Conf;
import com.sibertech.lib.conf.DSet;
import com.sibertech.lib.conf.ConfApp;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Gen {

    protected static Logger logback = LoggerFactory.getLogger(Gen.class);

    public void fillProducts (DSet dSet) throws Exception {
        GenAuxProducts gen = new GenAuxProducts();
        gen.generate(dSet);
        logback.warn(String.format("%s (%s) Генерация товаров завершена", ConfApp.LOG_PEFIX, Conf.mcServName()));
    }

    public void fillLoyalities (DSet dSet) throws Exception {
        GenAuxLoyalities gen = new GenAuxLoyalities();
        gen.generate(dSet);
        logback.warn(String.format("%s (%s) Генерация программы лояльности завершена", ConfApp.LOG_PEFIX, Conf.mcServName()));
    }

    public void fillClients (DSet dSet) throws Exception {
        GenAuxClients gen = new GenAuxClients();
        gen.generate(dSet);
        logback.warn(String.format("%s (%s) Генерация клиентов завершена", ConfApp.LOG_PEFIX, Conf.mcServName()));
    }

    public void fillRegions (DSet dSet) {

        Map<Integer, Region> regionsMap = dSet.getRegionsMap();
        GenRegions gen = new GenRegions();

        for (Region reg: regionsMap.values()) {
                gen.generate(dSet, reg);
                logback.warn(String.format("%s (%s) Генерация магазинов, терминалов и корзин продуктов для региона %02d завершена", ConfApp.LOG_PEFIX, Conf.mcServName(), reg.getId()));
        }
        logback.warn(String.format("%s :::::::::::::: (%s) Генерация регионов и вложенных данных завершена :::::::::::::: ", ConfApp.LOG_PEFIX, Conf.mcServName()));
    }
}

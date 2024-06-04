package com.sibertech.gen_srv.controller;

import com.sibertech.lib.params.http_client.GetParams;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sibertech.lib.Gen.db.ins.GenDbInsCore;
import com.sibertech.lib.Gen.db.ins.GenDbInsSales;
import com.sibertech.lib.conf.Conf;
import com.sibertech.lib.conf.ConfApp;
import com.sibertech.lib.db.DbClearSales;
import com.sibertech.lib.db.DbShowCount;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Микросервис генерации БД
 * Слушает на порту 8080
 */
@RestController
@RequestMapping("/dip-gen")
public class ContrGen {

    protected Logger logback = LoggerFactory.getLogger(ContrGen.class);
    protected ObjectMapper objectMapper = ConfApp.objectMapperInst();

    protected static Conf conf = Conf.inst("02.0-GEN-serv");

// ============================================================================================================================
    //  команда обратится к микросервису конфигурации за конфигурацией
    // но для генератора особой роли не играет. Можно использовать, для того, 
    // чтобы забрать имя каталога логгирования - сигнхронно с сервисом конфигурации
    // http://localhost:8080/dip-gen/get-config
    @GetMapping("/get-config")
    public ResponseEntity<String> getConfig () throws Exception {

        String mcServName = Conf.mcServName();

        String url = conf.app().getMc_conf_url (); // откуда запрашиваем параметры
        url = url + "?servRequested=" +  URLEncoder.encode(mcServName, StandardCharsets.UTF_8);

        GetParams getter = new GetParams();
        ConfApp params = getter.requestParams(url);
        conf.app().setConfForeign (params);

        // String jConf = objectMapper.writeValueAsString(params);
        String jConfPretty = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(conf.app());

        String msg = String.format("%s (%s) конфигурация получена", ConfApp.LOG_PEFIX, Conf.mcServName());
        logback.info("");
        logback.info(msg);
        logback.info (jConfPretty);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(msg + "<br>" + jConfPretty);
    }
// ============================================================================================================================
    // заполняются все вспомогательные таблицы (кроме таблиц продаж - sales и order_numbers_dictionary)
    // http://localhost:8080/dip-gen/insert-core
    @GetMapping("/insert-core")
    public ResponseEntity<String> insertCoreTables () throws Exception {

        GenDbInsCore mngr = new GenDbInsCore ();
        mngr.insertCoreTables(conf.data());

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(Conf.mcServName() + ": <br>Вставлены сгенерированные данные БАЗОВЫХ таблиц (insertCoreTables()), бд = " + conf.app().getDbName());
    }
 // ===========================================================================================================================
    // заполняются только таблицы продаж sales и order_numbers_dictionary
    // http://localhost:8080/dip-gen/insert-sales
    @GetMapping("/insert-sales")
    public ResponseEntity<String> insertSalesTables () throws Exception {

        GenDbInsSales mngr = new GenDbInsSales ();
        mngr.insertSalesTables(conf.data());

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(Conf.mcServName() + ": <br>Вставлены сгенерированные данные таблиц sales и dictionary (insertSalesTables()), бд = " + conf.app().getDbName());
    }
// ============================================================================================================================
    // выводит в консоль количество строк в таблицах sales и dictionary
    // http://localhost:8080/dip-gen/count-all-tables
    @GetMapping("/count-all-tables")
    public ResponseEntity<String> showCountAll () throws Exception {

        DbShowCount mngr = new DbShowCount ();
        String ret = mngr.showCountAll();

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(Conf.mcServName() + ": <br>showCountAll(), бд = " + conf.app().getDbName() + "<br>" + ret);
    }
// ============================================================================================================================
    // выводит в консоль количество строк в таблицах sales и dictionary
    // http://localhost:8080/dip-gen/count-sales
    @GetMapping("/count-sales")
    public ResponseEntity<String> showSalesCount () throws Exception {

        DbShowCount mngr = new DbShowCount ();
        String ret = mngr.showCountSales();

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(Conf.mcServName() + ": <br>showSalesCount(), бд = " + conf.app().getDbName() + "<br>" + ret);
    }
 // ===========================================================================================================================
    // выводит в консоль количество строк в  базовых таблицах
    // http://localhost:8080/dip-gen/count-core
    @GetMapping("/count-core")
    public ResponseEntity<String> showCoreCount () throws Exception {

        DbShowCount mngr = new DbShowCount ();
        String ret = mngr.showCountCore();

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(Conf.mcServName() + ": <br>showCoreCount(), бд = " + conf.app().getDbName() + "<br>" + ret);
    }
 // ===========================================================================================================================
/*
    // удаляет содержимое всех начальных таблиц
    // http://localhost:8080/dip-gen/delete-from-all-tables
    @GetMapping("/delete-from-all-tables")
    public ResponseEntity<String> deleteFromAllTables () throws Exception {

        DbDeleteFromCoreTables mngr = new DbDeleteFromCoreTables ();
        mngr.deleteFromCoreTables();

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(Conf.mcServName() + ": <br>deleteFromTables(), бд = " + conf.app().getDbName());
    }
// ============================================================================================================================
    // удаляет из таблиц sales и dictionary с суффиксами (__10 и тд) излишек - то есть удаляет из таблиц записи, чей id больше указанного в суффиксе
    // http://localhost:8080/dip-gen/remove-exceed-from-sales
    @GetMapping("/remove-exceed-from-sales")
    public ResponseEntity<String> removeExceedFromSales () throws Exception {

        DbRemoveExceedFromSales mngr = new DbRemoveExceedFromSales ();
        String res =  mngr.removeExceedFromSales();

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(Conf.mcServName() + ": <br>removeExceedFromSales(), бд = " + conf.app().getDbName() + " " + res);
    }
*/
    // ============================================================================================================================
    // удаляет из таблиц sales и dictionary с суффиксами (__10 и тд) излишек - то есть удаляет из таблиц записи, чей id больше указанного в суффиксе
    // http://localhost:8080/dip-gen/clear-sales
    @GetMapping("/clear-sales")
    public ResponseEntity<String> clearSales () throws Exception {

        DbClearSales mngr = new DbClearSales ();
        String res =  mngr.clearSales();

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(Conf.mcServName() + ": <br>clearSales(), бд = " + conf.app().getDbName() + " " + res);
    }
// ============================================================================================================================
}
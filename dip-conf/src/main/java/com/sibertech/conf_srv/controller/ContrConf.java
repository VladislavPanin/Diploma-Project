package com.sibertech.conf_srv.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sibertech.lib.conf.Conf;
import com.sibertech.lib.conf.ConfApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Микросервис провайдер конфигурации
 * Слушает на порту 8070
 */
@RestController
@RequestMapping("/dip-conf")
public class ContrConf {

    protected Logger logback = LoggerFactory.getLogger(ContrConf.class);
    protected ObjectMapper objectMapper = ConfApp.objectMapperInst();

    protected static Conf conf = Conf.inst("01.0-CONF-serv");

    // ============================================================================================================================
/*
http://localhost:8070/dip-conf/get?servRequested=postman
*/
    // какой-то сервис запросил параметры - отдаем ConfApp (conf.app()).
    // Имя сервера передается как параметр GET-запроса
    @GetMapping("/get")
    public ConfApp provideConf (@RequestParam(name = "servRequested") String servRequested) throws Exception {

        {// все что в скобках - это просто вывод в консоль.
         // отдает настройки одна строчка - "return conf.app();"
                //String jConf = objectMapper.writeValueAsString(conf.app());
                String jConfPretty = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(conf.app());

                String msg = String.format("%s (%s) Передана конфигурация микросервисов на сервис %s", ConfApp.LOG_PEFIX, Conf.mcServName(), servRequested);
                logback.info("");
                logback.info(msg);
                logback.info (jConfPretty);
        }
        return conf.app();
    }

    // ============================================================================================================================
/*
http://localhost:8070/dip-conf/set-params
*/
    // Запрос с postman - требование установить новый набор параметров, который будет раздаваться микросервисам по их запросу
    // Остальные микросервисы должны обновить свои конфигурации выполнив запрос /dip-conf/get и забрав с этого сервера полную конфигурацию
    @PostMapping("/set-params")
    public ResponseEntity<String> setParams (@RequestBody String jStr) throws Exception {

        ConfApp params = objectMapper.readValue(jStr.getBytes("UTF-8"), ConfApp.class);  // порождаем объект из строки JSON

        conf.app().setConfRoot (params);
        String jConfPretty = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(conf.app()); // можно было использовать jStr

        String msg = String.format("\nУстановлена новая конфигурация микросервисов -- ! -- Требуется обновить конфигурации каждого сервиса.\n%s",jConfPretty);
        logback.info(msg);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(msg);
    }
    // ===========================================================================================================================
}
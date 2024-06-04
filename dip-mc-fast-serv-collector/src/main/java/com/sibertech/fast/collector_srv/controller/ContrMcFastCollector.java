package com.sibertech.fast.collector_srv.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sibertech.fast.collector_srv.services.InsFastCollectorSales;
import com.sibertech.lib.conf.Conf;
import com.sibertech.lib.conf.ConfApp;
import com.sibertech.lib.params.http_client.GetParams;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Микросервис приемник данных от генератора отсылки данных
 * Слушает на порту 8090
 */
@RestController
@RequestMapping("/dip-fast-collector")
public class ContrMcFastCollector {

    protected Logger logback = LoggerFactory.getLogger(ContrMcFastCollector.class);
    protected ObjectMapper objectMapper = ConfApp.objectMapperInst();

    protected static Conf conf = Conf.inst("04.2-fast-collector");

    protected static ExecutorService threadPool = Executors.newFixedThreadPool(3);
    protected InsFastCollectorSales ins;

// ============================================================================================================================
    //  команда обратится к микросервису конфигурации за конфигурацией
    // http://localhost:8090/dip-fast-collector/get-config
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

            this.ins = new InsFastCollectorSales();
            threadPool.submit(ins);
            logback.info (String.format("%s (%s) Поток Коллектора быстрой вставки отправлен на выполнение", ConfApp.LOG_PEFIX, Conf.mcServName()));

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(msg + "<br>" + jConfPretty);
    }
// ============================================================================================================================
    // слушаем POST на предмет приема корзины для вставки в буфер
    // http://localhost:8090/dip-fast-collector/listen-for-basket
    @PostMapping("/listen-for-basket")
    public ResponseEntity listenForBasket (@RequestBody String jBasket) throws Exception {
        this.ins.add(jBasket);
        return ResponseEntity
            .status(HttpStatus.OK).build();
    }
}
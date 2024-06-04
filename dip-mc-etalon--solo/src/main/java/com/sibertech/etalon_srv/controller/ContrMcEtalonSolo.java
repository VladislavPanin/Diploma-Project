package com.sibertech.etalon_srv.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sibertech.etalon_srv.services.ThreadRunner;
import com.sibertech.lib.conf.Conf;
import com.sibertech.lib.conf.ConfApp;
import com.sibertech.lib.db.measure.DipTimer;
import com.sibertech.lib.params.http_client.GetParams;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Микросервис эталонной вставки напрямую в БД
 * Слушает на порту 8075
 */
@RestController
@RequestMapping("/dip-etalon")
public class ContrMcEtalonSolo {

    protected Logger logback = LoggerFactory.getLogger(ContrMcEtalonSolo.class);
    protected ObjectMapper objectMapper = ConfApp.objectMapperInst();

    protected DipTimer summaryTimer = null;

    protected static Conf conf = Conf.inst("03.1-etalon-solo");

// ============================================================================================================================
    //  команда обратится к микросервису конфигурации за конфигурацией
    // http://localhost:8075/dip-etalon/get-config
    @GetMapping("/get-config")
    public ResponseEntity<String> getConfig () throws Exception {

        String mcServName = Conf.mcServName();

        String url = conf.app().getMc_conf_url (); // откуда запрашиваем параметры
        url = url + "?servRequested=" +  URLEncoder.encode(mcServName, StandardCharsets.UTF_8);

        GetParams getter = new GetParams();
        ConfApp params = getter.requestParams(url);
        conf.app().setConfForeign (params);

                summaryTimer = new DipTimer();
                LocalDateTime ldt = conf.app().getStartInsertTime();
                summaryTimer.startAt(ldt);

        // String jConf = objectMapper.writeValueAsString(params);
        String jConfPretty = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(conf.app());

        String msg = String.format("%s (%s) конфигурация получена", ConfApp.LOG_PEFIX, Conf.mcServName());
        logback.info("");
        logback.info(msg);
        logback.info (jConfPretty);

        prepereThreadsStart();

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(msg + "<br>" + jConfPretty);
    }
// ===========================================================================================================================
/*
    // заполняются только таблицы продаж sales и order_numbers_dictionary
    // http://localhost:8075/dip-etalon/insert-sales
    @GetMapping("/insert-sales")
    public ResponseEntity<String> insertSalesTables () throws Exception {

        ThreadRunner runner = new ThreadRunner();
        String       htmlReport = runner.run_threads_etalon(conf.app().getDB_INSERTER_THREADS_COUNT());

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(Conf.mcServName() + ": <br>insertSalesTables(), бд = " + conf.app().getDbName() + "<br>" + htmlReport);
    }
*/
// ============================================================================================================================

    private void prepereThreadsStart() throws IOException, InterruptedException, ExecutionException {

        Duration duration = Duration.between(LocalDateTime.now(), conf.app().getStartInsertTime());

        long delay = duration.getSeconds();
        int threadCount = conf.app().getDB_INSERTER_THREADS_COUNT();

        logback.info(String.format("%s %d потоков будут запущены на выполнение вставки через %d секунд", ConfApp.LOG_PEFIX, threadCount, delay));

        ThreadRunner runner = new ThreadRunner();
        runner.schedule_threads_etalon(threadCount, delay, summaryTimer);
    }
}
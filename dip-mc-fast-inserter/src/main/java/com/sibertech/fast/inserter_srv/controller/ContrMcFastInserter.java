package com.sibertech.fast.inserter_srv.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sibertech.fast.inserter_srv.services.ThreadRunner;
import com.sibertech.lib.conf.Conf;
import com.sibertech.lib.conf.ConfApp;
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
 * Микросервис-генератор отсылки на сервис-приемник данных
 * Слушает на порту 8081
 */
@RestController
@RequestMapping("/dip-fast-inserter")
public class ContrMcFastInserter {

    protected Logger logback = LoggerFactory.getLogger(ContrMcFastInserter.class);
    protected ObjectMapper objectMapper = ConfApp.objectMapperInst();

    protected static Conf conf = Conf.inst("04.1-fast-inserter");

// ============================================================================================================================
    //  команда обратится к микросервису конфигурации за конфигурацией
    // http://localhost:8081/dip-fast-inserter/get-config
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
        
        prepereThreadsStart();

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(msg + "<br>" + jConfPretty);
    }
 
// ============================================================================================================================
    private void prepereThreadsStart() throws IOException, InterruptedException, ExecutionException {
        
        Duration duration = Duration.between(LocalDateTime.now(), conf.app().getStartInsertTime());
        
        long delay = duration.getSeconds();        
        int threadCount = conf.app().getDB_INSERTER_THREADS_COUNT();
        
        logback.info(String.format("%s %d потоков будут запущены на выполнение вставки через %d секунд", ConfApp.LOG_PEFIX, threadCount, delay));

        ThreadRunner runner = new ThreadRunner();
        runner.schedule_threads_fast(threadCount, delay);
    }
// ============================================================================================================================
      /*
    // слушаем POST на предмет приема корзины для вставки в буфер
    // http://localhost:8081/fast-inserter/listen-for-basket
    @PostMapping("/listen-for-basket")
    public String listenForBasket (@RequestBody String jBasket) throws Exception {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // слушаем POST на предмет приема корзины для вставки в буфер
    // http://localhost:8081/fast-inserter/listen-for-basket
    @PostMapping("/listen-for-basket")
    public ResponseEntity<String> listenForBasket (@RequestBody ProductBasket basket) throws Exception {

        Product prod;
        Map<Integer, Product> productMap = basket.getBasket();
        int count = productMap.size();
        for (int i = 1; i < count; i++)
        {
            prod = productMap.get(i);
            int id;
            id = prod.getCategory_id();
        }

        return ResponseEntity
            .status(HttpStatus.OK)
            .body("Все ок");
    }
*/
}
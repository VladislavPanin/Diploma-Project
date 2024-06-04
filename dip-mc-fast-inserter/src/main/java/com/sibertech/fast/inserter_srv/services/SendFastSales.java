package com.sibertech.fast.inserter_srv.services;

import com.sibertech.fast.inserter_srv.http.sender.ToFastCollectorSender;
import com.sibertech.lib.DTO.Market;
import com.sibertech.lib.DTO.Product;
import com.sibertech.lib.DTO.ProductBasket;
import com.sibertech.lib.DTO.Region;
import com.sibertech.lib.DTO.Terminal;
import com.sibertech.lib.conf.Conf;
import com.sibertech.lib.conf.ConfApp;
import com.sibertech.lib.conf.ConfDb;
import com.sibertech.lib.conf.ConfSQL;
import com.sibertech.lib.conf.DSet;
import com.sibertech.lib.conf.LogFactory;
import com.sibertech.lib.conf.LogWriter;
import com.sibertech.lib.db.actuator.DtoActuator;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// все определения - для генерации
public class SendFastSales {

    protected int thread_num = -1;
    protected Conf conf = Conf.inst();
    protected Connection  conn;

    protected DtoActuator actuator = new DtoActuator();
    
    protected String salesTable      = null;
    protected String dictionaryTable = null;

    protected String sqlOper_SALES      = null;
    protected String sqlOper_DICTIONARY = null;

    protected long startTime = 0L; // замер времени на всю вставку  в таблицу (для любых таблиц)
    protected long endTime = 0L;   // замер времени на всю вставку  в таблицу (для любых таблиц)

    protected long startTimeInterval_forInsert = 0L; // замер времени для построения графика при вставке в таблицы продаж (sales и dictionary)
    protected long endTimeInterval_forInsert = 0L;   // замер времени для построения графика при вставке в таблицы продаж (sales и dictionary)

    protected Logger logback = LoggerFactory.getLogger(SendFastSales.class);
    
    protected String serviceThreadInfo = null;
    protected String serv_IP = "no IP";
    
    protected LogWriter sendLogger = null;       // логгер отправки корзин
    
    protected ToFastCollectorSender sender = new ToFastCollectorSender ();
    
    protected ProductBasket local_basket = createLocalProductBasket(); // локальная корзина, изолированнавя в текущем потоке    
    
    protected int countOfSendBasket = 0;
    
    public    void incSentBasketCount () {
        countOfSendBasket++;
    }
    public int getCountOfSendBasked () {
        return countOfSendBasket;
    }
    // =============================================================================
    
    public void prepere (int thread_num, Connection conn) throws IOException {
        
        this.serviceThreadInfo = Conf.mcServName() + ", thread num#" + thread_num;

        this.conn = conn;
        this.startTimeInterval_forInsert = System.currentTimeMillis();

        this.sendLogger  = LogFactory.get_for_dip_inserted_rows (LogFactory.LOG_MISSION_FAST, thread_num);        

        this.thread_num = thread_num;
        this.serv_IP = Conf.inst().app().getIpHostThis();
        
        this.salesTable = Conf.inst().app().getSalesTableName();
        this.dictionaryTable = Conf.inst().app().getDictionaryTableName();
        
        this.sqlOper_SALES      = String.format (ConfSQL.INSERT_SALE,this.salesTable);
        this.sqlOper_DICTIONARY = String.format (ConfSQL.INSERT_BASKET_KEY, this.dictionaryTable);
    }
    
    public void insert_fast (int thread_number, DSet dSet, Connection conn) throws IOException {

        prepere (thread_number, conn);

        int iBasket = -1;
        int iTerminal = -1;
        int iMarket = -1;        
        int iRegion = this.thread_num + 1; // Число потоков вставки на одной машине должно быть равно числу регионов

        Terminal terminal;
        ProductBasket sharedBasket;
        Map<Integer, Region> regionsMap = dSet.getRegionsMap();

        boolean firstTime = true;

        try {
            do{
                for (iMarket = 1; iMarket <= ConfDb.COUNT_OF_MARKETS_IN_REGION; iMarket++) {
                    for (iTerminal = 1; iTerminal <= ConfDb.COUNT_OF_TERMINALS_IN_MARKET; iTerminal++) {
                        
                                Region region = regionsMap.get(iRegion);                        
                                if(firstTime) {
                                    iMarket = actuator.getRandomMarketId();
                                    iTerminal = actuator.getRandomTerminalId();
                                    firstTime = false;
                                }

                                terminal = getTerminal(region, iMarket, iTerminal);
                                Map<Integer,ProductBasket> productBasketMap = terminal.getProductBasketMap();

                                int basketSize = productBasketMap.size();
                                for (iBasket = 1; iBasket <= basketSize; iBasket++) {

                                    sharedBasket = productBasketMap.get(iBasket); // корзина, общая для всех потоков
                                    if(!insertBasket_onLogger (sharedBasket)) {
                                        return;
                                    }
                                }
                    }
                }
            }
            while(true);
        }
        catch(Exception exc) {

            String msg = String.format("(%s) iRegion= %d  iMarket= %d  iTerminal= %d iBasket= %d", this.serviceThreadInfo, iRegion, iMarket, iTerminal, iBasket);
            String msgex = String.format("Исключение в методе InsFastSales::insert_fast(): %s", exc.getMessage());
            logback.warn(String.format("%s %s", ConfApp.LOG_PEFIX_EXCEPT, msg));
            logback.warn(String.format("%s %s", ConfApp.LOG_PEFIX_EXCEPT, msgex));
        }
    }
     
    protected boolean insertBasket_onLogger (ProductBasket sharedBasket) throws IOException {

        boolean ret = true;
        try{
            insertBasket(sharedBasket);
            actuator.incOrderNumber();
        }
        catch (Exception exc) {
            String msg = exc.getMessage();
            logback.warn(String.format("%s (%s) %s -> %s", ConfApp.LOG_PEFIX_EXCEPT, this.serviceThreadInfo, "InsFastSales::insertBasket_onLogger()", msg));
        }
        finally {
            int countOfSendLines = countOfSendBasket * ConfDb.COUNT_OF_PRODUCTS_IN_BASKET; 
            if (countOfSendLines % conf.app().getInsertStepCounter()== 0 && countOfSendLines != 0) {
                // количество продуктов в корзине должно быть кратно 10 - иначе сюда никогда не попадем

                endTimeInterval_forInsert = System.currentTimeMillis();
                int intervalOnInserts = (int)(endTimeInterval_forInsert - startTimeInterval_forInsert);
                
                sendLogger. _log(countOfSendLines, intervalOnInserts); // вывод графика зависимости времени вставки строки от количества отправок корзин

                startTimeInterval_forInsert = endTimeInterval_forInsert;
            }
            if(countOfSendLines >= conf.app().getBreakDip_afterCount())
                ret = false;
        }
        return ret;
    }
    
    protected void insertBasket (ProductBasket sharedBasket) throws Exception {

            // перекладка общей корзины в локальную, актуализация данных в ней
           local_basket.setEqual(sharedBasket);
           int clientId = this.actuator.getRandomClientId ();
           local_basket.setClient_id(clientId);

            int basketPrice = 0;
            Map<Integer, Product>    productsMap = this.local_basket.getBasket();

            for (int iLineNumber = 1; iLineNumber <= productsMap.size(); iLineNumber++) {
                Product product = productsMap.get(iLineNumber);
                int count = this.actuator.getPiecesCount();
                product.setQuantityInBasket(count);
                basketPrice += count*product.getCost();
            }
            local_basket.setBasket_price(basketPrice);
            local_basket.setSaleTime (this.actuator.calcTime());
            local_basket.setSaleDate (this.actuator.calcDate());
            
            sender.send (thread_num, local_basket);
            this.incSentBasketCount();
    }
    
    // =============================================================================
    private ProductBasket createLocalProductBasket() {

        ProductBasket basket = new ProductBasket();
        for(int i=1; i <= ConfDb.COUNT_OF_PRODUCTS_IN_BASKET; i++) {
            Product product = new Product();
            basket.addProduct(product);
        }
        return basket;
    }
    
    protected Terminal getTerminal(Region region, int iMarket, int iTerminal) {

        Map<Integer, Market> marketsMap = region.getMarketsMap();
        Market market = marketsMap.get(iMarket);
        Map<Integer,Terminal>  terminalsMap = market.getTerminals_in_market();
        Terminal terminal = terminalsMap.get(iTerminal);

        return terminal;
    }
}

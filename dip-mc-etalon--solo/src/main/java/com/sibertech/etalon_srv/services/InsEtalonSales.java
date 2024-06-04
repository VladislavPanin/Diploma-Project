package com.sibertech.etalon_srv.services;

import com.sibertech.etalon_srv.http.sender.ToSummSender;
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
import com.sibertech.lib.db.measure.MeasureTimeouts;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsEtalonSales {

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

    protected Logger logback = LoggerFactory.getLogger(InsEtalonSales.class);

    protected String serviceThreadInfo = null;
    protected String serv_IP = "no IP";

    protected LogWriter insertsLogger = null;       // логгер замеров для графиков (выводит в файл и в консоль одновременно)
    protected LogWriter timeoutsLogger = null;       // логгер замеров для графиков (выводит в файл и в консоль одновременно)

    protected ToSummSender sender = new ToSummSender ();

    protected ProductBasket local_basket = createLocalProductBasket(); // локальная корзина, изолированнавя в текущем потоке
    protected MeasureTimeouts measureTimeouts = null;
    // =============================================================================

    protected int timeoutsCounter = 0;
    protected int countOfInsertedLines = 0;

    public    void incLineCount () {
        countOfInsertedLines++;
    }
    public int getCountOfInsertedLines () {
        return countOfInsertedLines;
    }
    // =============================================================================
    public InsEtalonSales (MeasureTimeouts measureTimeouts) {
        this.measureTimeouts = measureTimeouts;
    }

    public void prepere (int thread_num, Connection conn) throws IOException {

        this.serviceThreadInfo = Conf.mcServName() + ", thread num#" + thread_num;

        this.conn = conn;
        this.startTimeInterval_forInsert = System.currentTimeMillis();

        this.insertsLogger  = LogFactory.get_for_dip_inserted_rows (LogFactory.LOG_MISSION_ETAL, thread_num);
        this.timeoutsLogger = LogFactory.get_for_dip_timeout_count (LogFactory.LOG_MISSION_ETAL, thread_num);

        this.insertsLogger.start();
        this.timeoutsLogger.start();

        this.thread_num = thread_num;
        this.serv_IP = Conf.inst().app().getIpHostThis();

        this.salesTable = Conf.inst().app().getSalesTableName();
        this.dictionaryTable = Conf.inst().app().getDictionaryTableName();

        this.sqlOper_SALES      = String.format (ConfSQL.INSERT_SALE,this.salesTable);
        this.sqlOper_DICTIONARY = String.format (ConfSQL.INSERT_BASKET_KEY, this.dictionaryTable);
    }

    public void insert_etalon (int thread_number, DSet dSet, Connection conn) throws IOException {

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
            String msgex = String.format("Исключение в методе InsEtalonSales::insert_etalon(): %s", exc.getMessage());
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
            logback.warn(String.format("%s (%s) %s -> %s", ConfApp.LOG_PEFIX_EXCEPT, this.serviceThreadInfo, "InsEtalonSales::insertBasket_onLogger()", msg));
        }
        finally {

            if (countOfInsertedLines % conf.app().getInsertStepCounter()== 0 && countOfInsertedLines != 0) {
                // количество продуктов в корзине должно быть кратно 10 - иначе сюда никогда не попадем

                endTimeInterval_forInsert = System.currentTimeMillis();
                int intervalOnInserts = (int)(endTimeInterval_forInsert - startTimeInterval_forInsert);

                insertsLogger. _log(countOfInsertedLines, intervalOnInserts); // вывод графика зависимости времени вставки строки от количества строк в таблице
                timeoutsLogger._log(countOfInsertedLines, timeoutsCounter);

                sender.send_inserted(countOfInsertedLines, intervalOnInserts, this.thread_num, this.serv_IP);
                sender.send_timeouts(countOfInsertedLines, timeoutsCounter, this.thread_num, this.serv_IP);
                measureTimeouts.addTimeouts(timeoutsCounter);

                startTimeInterval_forInsert = endTimeInterval_forInsert;
                timeoutsCounter = 0;
            }
            if(countOfInsertedLines >= conf.app().getBreakDip_afterCount())
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

            // корзина сформирована, пытаемся вставить в БД
                tryInsertBasket ();
    }

    // =========================================================================
    private static final String SQL_START_TRANSACTION = "BEGIN TRANSACTION";
    private static final String SQL_ROLLBACK = "ROLLBACK";
    private static final String SQL_LOCK_TBL_PATTERN = "LOCK TABLE %s";

    // вставляем одну корзину, в транзакции с лок-тейбл
    protected void tryInsertBasket () throws Exception {

        while(true)
        {
            Statement  stmt = conn.createStatement();
            boolean success = true;
            try
            {
                String sLockSales = String.format(SQL_LOCK_TBL_PATTERN, this.salesTable);
                String sLockDicts = String.format(SQL_LOCK_TBL_PATTERN, this.dictionaryTable);
                String sLockClnts = String.format(SQL_LOCK_TBL_PATTERN, "clients");

                stmt.execute(SQL_START_TRANSACTION);
                stmt.execute(sLockSales);
                stmt.execute(sLockDicts);
                stmt.execute(sLockClnts);
                             tryInsertBasket_internal ();
                stmt.execute("COMMIT"); // Команды UNLOCK TABLE не существует; блокировки всегда освобождаются в конце транзакции
            }
            catch (Exception exc)
            {
                String msg  = exc.getMessage();
                timeoutsCounter++;
                // logback.warn(String.format("%s (%s) %s -> %s", ConfApp.LOG_PEFIX_EXCEPT, this.serviceThreadInfo, "InsEtalonSales::tryInsertBasket() external block", msg));
                stmt.execute(SQL_ROLLBACK);
                success = false;
                this.timeoutsCounter++;
            }
            finally  {stmt.close();}

            if (success)
                return;
        }
    }

    protected void tryInsertBasket_internal () throws Exception {
            int
            order_number = insertDictionary_getReturningOrder_number(this.local_basket);
            this.local_basket.setOrder_number(order_number);
            insertSales (this.local_basket);
            updateLoyality (local_basket.getRegion_id(), local_basket.getClient_id());
    }

    protected void insertSales (ProductBasket basket) throws Exception {
            Map<Integer, Product>    productsMap = basket.getBasket();

            for (int iLineNumber = 1; iLineNumber <= productsMap.size(); iLineNumber++) {
                Product product = productsMap.get(iLineNumber);

                //logback.warn(SQL.get_INSERT_SALE_SQL_VALUE(this.salesTable, iLineNumber, product, basket));
                try (PreparedStatement preparedStatement = this.conn.prepareStatement(this.sqlOper_SALES);) {

                    preparedStatement.setInt   (1,  basket.getOrder_number());
                    preparedStatement.setInt   (2,  iLineNumber);
                    preparedStatement.setInt   (3,  basket.getRegion_id());
                    preparedStatement.setInt   (4,  basket.getMarket_id());
                    preparedStatement.setInt   (5,  basket.getTerminal_id());
                    preparedStatement.setInt   (6,  product.getId());
                    preparedStatement.setInt   (7,  product.getQuantityInBasket());
                    preparedStatement.setInt   (8,  product.getCost());
                    preparedStatement.setDate  (9,  basket.getSaleDate());
                    preparedStatement.setInt   (10, basket.getClient_id());
                    preparedStatement.setString(11, basket.getComment());

                    preparedStatement.execute();
                    this.incLineCount();  // для общего логгирования всех таблиц
                }
            }
    }

    protected int insertDictionary_getReturningOrder_number (ProductBasket productBasket) throws Exception {

        int order_number = -1;

        //logback.warn(ConfSQL.get_INSERT_BASKET_KEY_SQL_VALUE(this.dictionaryTable, productBasket));
        try(PreparedStatement preparedStatement = this.conn.prepareStatement(this.sqlOper_DICTIONARY);) {

            preparedStatement.setInt      (1, productBasket.getRegion_id());
            preparedStatement.setInt      (2, productBasket.getMarket_id());
            preparedStatement.setInt      (3, productBasket.getTerminal_id());
            preparedStatement.setInt      (4, productBasket.getTerminal_basket_number());
            preparedStatement.setInt      (5, productBasket.getBasket_price());
            preparedStatement.setTime     (6, productBasket.getSaleTime());
            preparedStatement.setDate     (7, productBasket.getSaleDate());
            preparedStatement.setString   (8, productBasket.getComment());

                try(ResultSet resultSet = preparedStatement.executeQuery();) {   //ВСТАВКА НЕ БУДЕТ ВОСПРОИЗВЕДЕНА, ЕСЛИ УЖЕ ЕСТЬ ТАКАЯ ЖЕ ЗАПИСЬ
                    if (resultSet.next()) {
                        order_number = resultSet.getInt(1);
                        return order_number;
                    }
                }
        }
        // если не сделали "return order_number;" - то что-то пошло не так. А если при этом не вылетели по исключению выше - бросаем его здесь
        throw new Exception(String.format ("(%s) вставка в таблицу dictionary не была произведена. ", this.serviceThreadInfo));
    }

    private static final String UPDATE_BONUSES  =
"""
WITH
   koef AS (SELECT min(coefficient_by_region)                 AS  min_koef   FROM loyalities WHERE region_id=?),
   summ AS (SELECT sum(price_of_one_piece * count_of_product) AS  price_summ FROM %s         WHERE client_id=?)
UPDATE
	clients
SET
	count_of_bonuses = COALESCE(koef.min_koef, 0)*COALESCE(summ.price_summ, 0)
FROM
	koef, summ
WHERE
	clients.id=?;
""";

    private void updateLoyality(int region_id, int client_id) throws SQLException {


        String updateOper = String.format(UPDATE_BONUSES, this.salesTable);
        try(PreparedStatement preparedStatement = this.conn.prepareStatement(updateOper);) {
            preparedStatement.setInt      (1, region_id);
            preparedStatement.setInt      (2, client_id);
            preparedStatement.setInt      (3, client_id);

            preparedStatement.executeUpdate();
        }
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

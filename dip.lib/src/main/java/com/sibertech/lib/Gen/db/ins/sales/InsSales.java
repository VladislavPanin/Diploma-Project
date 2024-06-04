package com.sibertech.lib.Gen.db.ins.sales;

import com.sibertech.lib.DTO.Market;
import com.sibertech.lib.DTO.Product;
import com.sibertech.lib.DTO.ProductBasket;
import com.sibertech.lib.DTO.Region;
import com.sibertech.lib.DTO.Terminal;
import com.sibertech.lib.Gen.db.ins.core.Ins;
import com.sibertech.lib.conf.DSet;
import com.sibertech.lib.conf.ConfDb;
import com.sibertech.lib.conf.ConfApp;
import com.sibertech.lib.conf.ConfSQL;
import com.sibertech.lib.db.actuator.DtoActuator;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

// все определения - для генерации
public class InsSales  extends Ins {

    protected DtoActuator actuator = new DtoActuator();
    protected Connection  conn;

    protected String salesTable      = null;
    protected String dictionaryTable = null;

    protected String sqlOper_SALES      = null;
    protected String sqlOper_DICTIONARY = null;

    // AllData - общая на все потоки. Но требуется менять значения корзины в конкретном потоке, и уже их вставлять в БД.
    // в корзине есть экземпляры Product, тое общие на все потоки.
    // Чтобы один поток не затер данные другого потока в ProductBasket и Product-ах, используем локальный экемпляр текущего потока,
    // который будет забирать основные данные из общего (AlData) и менять только те, которые нужно.
    // уже измененный локальный экземпляр ProductBasket-а используем для передачи между методами и из него будем вставлять данные в БД
    protected ProductBasket local_basket = createLocalProductBasket(); // локальная корзина, изолированнавя в текущем потоке

    //////// Тот, кто создал объект и запускает insert(), должен знать, в каке таблицы нужно вставлять - с суффиксом _10 или без него
    //////// он и должен вызвать этот метод и установить имена таблиц sales и dictionary
    public void prepare (String salesTable, String dictionaryTable)
    {
        this.salesTable = salesTable;
        this.dictionaryTable = dictionaryTable;

        this.sqlOper_SALES      = String.format (ConfSQL.INSERT_SALE,this.salesTable);
        this.sqlOper_DICTIONARY = String.format (ConfSQL.INSERT_BASKET_KEY, this.dictionaryTable);
    }
    ////////

    @Override
    public void insert_internal (DSet dSet, Connection conn) throws SQLException {

        this.conn = conn;
        this.startTimeInterval_forInsert = System.currentTimeMillis();

        int iBasket = -1;
        int iTerminal = -1;
        int iMarket = -1;
        int iRegion = -1;

        Terminal terminal;
        ProductBasket sharedBasket;
        Map<Integer, Region> regionsMap = dSet.getRegionsMap();

        try {
            do{
                for (iMarket = 1; iMarket <= ConfDb.COUNT_OF_MARKETS_IN_REGION; iMarket++) {
                    for (iTerminal = 1; iTerminal <= ConfDb.COUNT_OF_TERMINALS_IN_MARKET; iTerminal++) {
                        for (Map.Entry<Integer, Region> entry : regionsMap.entrySet()) {

                                Region region = entry.getValue();
                                iRegion = region.getId();

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
            }
            while(true);
        }
        catch(Exception exc) {

            String msg = String.format("(%s) iRegion= %d  iMarket= %d  iTerminal= %d iBasket= %d", this.serviceThreadInfo, iRegion, iMarket, iTerminal, iBasket);
            String msgex = String.format("Исключение в методе InsSales::insert(): %s", exc.getMessage());
            logback.warn(String.format("%s %s", ConfApp.LOG_PEFIX_EXCEPT, msg));
            logback.warn(String.format("%s %s", ConfApp.LOG_PEFIX_EXCEPT, msgex));
        }
    }

    // это для вставки генератором. Для других случаем - переопределить
    protected boolean insertBasket_onLogger (ProductBasket sharedBasket) throws IOException {
        boolean ret = true;
        try{
            insertBasket(sharedBasket);
            actuator.incOrderNumber();
        }
        catch (Exception exc) {

            logback.warn(String.format("%s (%s) %s -> %s", ConfApp.LOG_PEFIX_EXCEPT, this.serviceThreadInfo, "InsSales::insertBasket_onLogger()", exc.getMessage()));
        }
        finally {

            if (countOfInsertedLines % conf.app().getInsertStepCounter()== 0) {
                // количество продуктов в корзине должно быть кратно 10 - иначе сюда никогда не попадем
                endTimeInterval_forInsert = System.currentTimeMillis();
                insertedRowsLogger._log (countOfInsertedLines, endTimeInterval_forInsert - startTimeInterval_forInsert); // вывод графика зависимости времени вставки строки от количества строк в таблице
                startTimeInterval_forInsert = endTimeInterval_forInsert;
            }
            if(countOfInsertedLines >= conf.app().getBreakGen_afterCount())
                ret = false;
        }
        return ret;
    }

    protected void insertBasket (ProductBasket sharedBasket) throws Exception {

            // перекладка общей корзины в локальную, актуализация данных в ней
            this.local_basket.setEqual(sharedBasket);
            int clientId = this.actuator.getRandomClientId ();
           this.local_basket.setClient_id(clientId);

            int basketPrice = 0;
            Map<Integer, Product>    productsMap = this.local_basket.getBasket();

            for (int iLineNumber = 1; iLineNumber <= productsMap.size(); iLineNumber++) {
                Product product = productsMap.get(iLineNumber);

                int count = this.actuator.getPiecesCount();
                product.setQuantityInBasket(count);
                basketPrice += count*product.getCost();
            }

            this.local_basket.setBasket_price(basketPrice);

            this.local_basket.setSaleTime (this.actuator.calcTime());
            this.local_basket.setSaleDate (this.actuator.calcDate());

            this.tryInsertBasket ();
    }

    protected void tryInsertBasket () throws Exception {
        // запуск корзины на вставку в БД
            int
            order_number = insertDictionary_getReturningOrder_number(this.local_basket);
            this.local_basket.setOrder_number(order_number);

            insertSales (this.local_basket);
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
                        int order_number = resultSet.getInt(1);
                        return order_number;
                    }
                }
        }
        catch (Exception exc) {
            String msg = exc.getMessage();
            int i = 1;
        }
        throw new Exception(String.format ("(%s) вставка в таблицу dictionary не была произведена", this.serviceThreadInfo));
    }

    protected Terminal getTerminal(Region region, int iMarket, int iTerminal) {

        Map<Integer, Market> marketsMap = region.getMarketsMap();
        Market market = marketsMap.get(iMarket);
        Map<Integer,Terminal>  terminalsMap = market.getTerminals_in_market();
        Terminal terminal = terminalsMap.get(iTerminal);

        return terminal;
    }

    private ProductBasket createLocalProductBasket() {

        ProductBasket basket = new ProductBasket();
        for(int i=1; i <= ConfDb.COUNT_OF_PRODUCTS_IN_BASKET; i++) {
            Product product = new Product();
            basket.addProduct(product);
        }
        return basket;
    }
}

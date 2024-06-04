package com.sibertech.fast.buff.proc_srv.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sibertech.lib.DTO.Product;
import com.sibertech.lib.DTO.ProductBasket;
import com.sibertech.lib.conf.Conf;
import com.sibertech.lib.conf.ConfApp;
import com.sibertech.lib.conf.ConfDb;
import com.sibertech.lib.conf.ConfSQL;
import com.sibertech.lib.conf.LogFactory;
import com.sibertech.lib.conf.LogWriter;
import com.sibertech.lib.conf.LogWriterSummary;
import com.sibertech.lib.db.DB;
import com.sibertech.lib.db.measure.DipTimer;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsFastBuffProc extends DB implements Callable {

    protected LoyalityProc loyalityProc = new LoyalityProc();

    protected Conf conf = Conf.inst();
    protected ObjectMapper objectMapper = ConfApp.objectMapperInst();
    protected static Logger logback = LoggerFactory.getLogger(InsFastBuffProc.class);

    protected ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue();
    protected int countToInsert = conf.app().getBreakDip_afterCount()*conf.app().getDB_INSERTER_THREADS_COUNT_AT_ALL();

    protected long startTimeInterval_forInsert = 0L; // замер времени для построения графика при вставке в таблицы продаж (sales и dictionary)
    protected long endTimeInterval_forInsert = 0L;   // замер времени для построения графика при вставке в таблицы продаж (sales и dictionary)

    protected DipTimer summaryTimer = null;
    protected LogWriterSummary logInsTime = null;
    protected LogWriter insertsLogger = null;       // логгер замеров для графиков (выводит в файл и в консоль одновременно)
    protected int countOfInsertedLines = 0;

    protected String salesTable = Conf.inst().app().getSalesTableName();
    protected String dictionaryTable = Conf.inst().app().getDictionaryTableName();

    protected String sqlOper_SALES = String.format (ConfSQL.INSERT_SALE,this.salesTable);
    protected String sqlOper_DICTIONARY = String.format (ConfSQL.INSERT_BASKET_KEY, this.dictionaryTable);
    // =============================================================================
    int step = conf.app().getInsertStepCounter();
    public    void incLinesCount () throws IOException {
        countOfInsertedLines++;

        if ((countOfInsertedLines % step)== 0 && countOfInsertedLines != 0) {

            endTimeInterval_forInsert = System.currentTimeMillis();
            int intervalOnInserts = (int)(endTimeInterval_forInsert - startTimeInterval_forInsert);
            insertsLogger. _log(countOfInsertedLines, intervalOnInserts); // вывод графика зависимости времени вставки строки от количества строк в таблице
            startTimeInterval_forInsert = endTimeInterval_forInsert;
        }
    }
    public int getCountOfInsertedLines () {
        return countOfInsertedLines;
    }
    // =============================================================================
    boolean isIdle = false;
    @Override
    public Object call() throws Exception {

        insertsLogger = LogFactory.get_for_dip_buff_proc();

        ConfApp app = Conf.inst().app();
        String thread_result = "запущен, работает";

        String connURL = ConfDb.BASE_DB_URL + app.getDbName();
        this.init(connURL, ConfDb.PG_DB_USER, ConfDb.PG_DB_PWD);

            logback.warn(String.format("%s (%s) Запущен  поток обработки буффера buffer_in , БД  -- %s -- (%,10d записей), таблица %s",
                    ConfApp.LOG_PEFIX,
                    Conf.mcServName(),
                    app.getDbName(),
                    this.countToInsert,
                    salesTable
            ));

            int countAlreadyInserted;
            try {
                    do {
                        process ();
                        countAlreadyInserted = this.getCountOfInsertedLines();
                    }
                    while(this.countToInsert > countAlreadyInserted);

                    summaryTimer.finish();
                    writeLog();
            }
            catch(Exception exc)
            {
                String msg = exc.getMessage();
                logback.error(String.format("%s InsFastBuffProc.call(): %s", ConfApp.LOG_PEFIX_EXCEPT, msg));
            }

            int spentTime = summaryTimer.getSpentTimeMillisec();

            logback.warn(String.format("%s (%s) Завершен поток обработки буффера buffer_in", ConfApp.LOG_PEFIX, Conf.mcServName()));
            logback.warn(String.format("%s (%s) Вставлено %d записей в таблицу %s, время вставки %d микросекунд \n", ConfApp.LOG_PEFIX, Conf.mcServName(), countToInsert, salesTable, spentTime));
            logback.warn(String.format("%s  =============== Новый поток обработки буффера будет запущен при обновлении конфигурации =============== ", ConfApp.LOG_PEFIX));
            logback.warn(String.format("%s  ======================================================================================================= ", ConfApp.LOG_PEFIX));

        this.close();
        return thread_result;
    }
    // =============================================================================
    private boolean firstTime = true;
    protected void process () throws SQLException, IOException, Exception {

        if (firstTime) {
            loyalityProc.prepere(conn);
            firstTime = false;
        }
        if (process_step () > 0) {
            return;
        }
        try {
            if(!isIdle) {
                logback.warn(String.format("%s InsFastBuffProc.process(): вошли в состояние ожидания - данных для обработки нет (в таблице buffer_in)", ConfApp.LOG_PEFIX));
            }
            isIdle = true;
            Thread.sleep(1000);
        }
        catch (Exception dummy) {
            String msg = dummy.getMessage();
            int i = 0;
        }
    }

    protected int process_step() throws Exception {

        reallocateData ();
        BufferInfo bufferInfo = getBufferInfo();

        if(bufferInfo.getCount()<1)
            return 0;

        int processedNum = processData(bufferInfo);

        return processedNum;
    }

    private static final String SQL_START_TRANSACTION = "BEGIN TRANSACTION";
    private static final String SQL_ROLLBACK = "ROLLBACK";
    private static final String SQL_LOCK_TBL_BUFFER = "LOCK TABLE buffer_in";
    private static final String SQL_INSERT_AS_SELECT =  "INSERT INTO buffer_processing SELECT * FROM buffer_in";
    private static final String SQL_DELETE_FROM_IN =  "DELETE FROM buffer_in";

    // вставляем одну корзину, в транзакции с лок-тейбл
    protected void reallocateData () throws Exception {

        Statement  stmt = conn.createStatement();
        boolean success = true;
        try
        {
            stmt.execute(SQL_START_TRANSACTION);
            stmt.execute(SQL_LOCK_TBL_BUFFER);
            stmt.execute(SQL_INSERT_AS_SELECT);
            stmt.execute(SQL_DELETE_FROM_IN);
            stmt.execute("COMMIT");
        }
        catch (Exception exc)
        {
            String msg  = exc.getMessage();
            stmt.execute(SQL_ROLLBACK);
        }
        finally
        {stmt.close();}
    }

    String sqlOperBufferInfo = "SELECT COALESCE(min(id),0) min_id, COALESCE(max(id),0) max_id, count(*) count_recs FROM buffer_processing";
    private BufferInfo getBufferInfo() throws SQLException {

        BufferInfo bufferInfo = new BufferInfo();

        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sqlOperBufferInfo);)
        {
            try(ResultSet resultSet = preparedStatement.executeQuery();) {
                    if (resultSet.next()) {
                        int min_id = resultSet.getInt("min_id");
                        int max_id = resultSet.getInt("max_id");
                        int count_recs = resultSet.getInt("count_recs");

                        bufferInfo.setCount(count_recs);
                        bufferInfo.setMaxID(max_id);
                        bufferInfo.setMinID(min_id);
                    }
                }
        }
        return bufferInfo;
    }


    String sqlOperSelectNextPart = "SELECT obj_record FROM buffer_processing WHERE id<=?";
    String sqlOperDeleteWasProcessed = "DELETE FROM buffer_processing WHERE id<=?";

    boolean firstEnteringIntoProcessing = true;
    private int processData(BufferInfo bufferInfo) throws SQLException, IOException, Exception {

        if(isIdle) {
                logback.warn(String.format("%s InsFastBuffProc.process(): ВЫШЛИ из состояния ожидания - появились данные для обработки (в таблице buffer_in)", ConfApp.LOG_PEFIX));
        }
        isIdle = false;

        if (firstEnteringIntoProcessing) {
            insertsLogger.start();
            startTimeInterval_forInsert = System.currentTimeMillis();
            summaryTimer.start();
            firstEnteringIntoProcessing = false;
        }

        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sqlOperSelectNextPart);)
        {
            preparedStatement.setInt(1,  bufferInfo.getMaxID());
            // основная обработка данных
            try(ResultSet resultSet = preparedStatement.executeQuery();) {
                while (resultSet.next()) {
                    process_result_set(resultSet); // по одной корзине за раз
                }
                loyalityProc.updateLoyality(conn);
            }
        }

        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sqlOperDeleteWasProcessed);)
        {
            preparedStatement.setInt(1,  bufferInfo.getMaxID());
            preparedStatement.execute();
        }

        return 1;
    }

    private void process_result_set(ResultSet rs) throws SQLException, UnsupportedEncodingException, IOException, Exception {

        String jStr = rs.getString("obj_record");

        ProductBasket basket = objectMapper.readValue(jStr.getBytes("UTF-8"), ProductBasket.class);  // порождаем объект из строки JSON
        int
        order_number = insertDictionary_getReturningOrder_number(basket);
        basket.setOrder_number(order_number);
        insertSales (basket);
        loyalityProc.collectLoyality (basket);
    }

    protected void insertSales (ProductBasket basket) throws Exception {
        try {
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
                    this.incLinesCount();
                }
            }
        }
        catch(Exception exc)
        {
            String str = exc.getMessage();
            System.out.println(str);
        }
    }

    protected int insertDictionary_getReturningOrder_number (ProductBasket productBasket) throws Exception {
        /*
        "INSERT INTO order_numbers_dictionary___500
(region_id, market_id, terminal_id, terminal_basket_number, basket_price, order_time, order_date, comment)
VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING order_number; */

        //logback.warn(ConfSQL.get_INSERT_BASKET_KEY_SQL_VALUE(this.dictionaryTable, productBasket));
        int order_number = -1;

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
        catch(Exception exc)
        {
            String str = exc.getMessage();
            System.out.println(str);
        }
        // если не сделали "return order_number;" - то что-то пошло не так. А если при этом не вылетели по исключению выше - бросаем его здесь
        throw new Exception("insertDictionary_getReturningOrder_number()) вставка в таблицу dictionary не была произведена. ");
    }

    protected void writeLog() throws IOException {
        int time = summaryTimer.getSpentTimeMillisec();
        String salesTableName = conf.app().getSalesTableName();
        int rowsInTable = conf.app().getCountLines_in_sales__xxxx();
        int insertedRows = conf.app().getBreakDip_afterCount () * conf.app().getDB_INSERTER_THREADS_COUNT_AT_ALL();

        logInsTime._logInsTime(salesTableName, insertedRows, rowsInTable, time);
    }

    public void prepare (LogWriterSummary logInsTime, DipTimer summaryTimer) {
        this.logInsTime = logInsTime;
        this.summaryTimer = summaryTimer;
    }

}

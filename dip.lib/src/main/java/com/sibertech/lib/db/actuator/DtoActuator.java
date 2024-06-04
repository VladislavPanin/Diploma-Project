package com.sibertech.lib.db.actuator;

import com.sibertech.lib.conf.ConfDb;
import com.sibertech.lib.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.ParseException;
import java.time.LocalDate;

public class DtoActuator {

    protected Logger logback = LoggerFactory.getLogger(DtoActuator.class);
    protected int sale_date_counter = 0;

    protected LocalDate      localDateRunner;
    protected java.sql.Date  sqlDateRunner;

    protected java.sql.Time  sqlTimeRunner;
    protected long           seconds = 0;


    // -----------------------------------------------------------------------
    protected int orderNumberCunterForDate = 1;    // для рассчета даты и времени
    public void incOrderNumber() {
        orderNumberCunterForDate++;

        // в 10 часах 36 000 секунд. Для наполнения таблиц при генерации, будем записывать, что продаем корзину каждые 10 секунд (sqlTimeRunner + 10 секунд).
        // Отсюда - продав 3 600 корзин, ставим следующую дату
        // 1 млн строк sales (100 тысяч в dictionary) будут проданы за 28 дней
        if (orderNumberCunterForDate > 3600) {

            orderNumberCunterForDate = 0;
            localDateRunner = localDateRunner.plusDays(1);
            sqlDateRunner  =  java.sql.Date.valueOf (localDateRunner);
            seconds = 0; // начались следующие сутки, время обнуляем
        }
        seconds++;
        sqlTimeRunner.setTime(seconds * 10000);  // *10 000 миллисекунд
    }
    // -----------------------------------------------------------------------
    // -----------------------------------------------------------------------
    public DtoActuator () {

        localDateRunner = LocalDate.parse("2000-01-01");
        sqlDateRunner =  java.sql.Date.valueOf (localDateRunner);

        sqlTimeRunner = java.sql.Time.valueOf("00:00:01");
        sqlTimeRunner.setTime(0); // будет установлен сдвиг по UTC
    }

    public java.sql.Date calcDate() {
        return sqlDateRunner;
    }

    public java.sql.Time calcTime() {
        return sqlTimeRunner;
    }
    // -----------------------------------------------------------------------
    // -----------------------------------------------------------------------
    public double getCoeff (double val) {
        double precission  = 100.0;  // округляем до 2-х знаков после запятой
        val = Math.ceil (val * precission);
        val = val / precission;
        return val;
    }

    public int getRandomClientId () throws Exception {
        int id = Util._getRandFromRange(1, ConfDb.COUNT_OF_CLIENTS);
        return id;
    }

    public int getRandomTerminalId () throws Exception {
        int id = Util._getRandFromRange(1, ConfDb.COUNT_OF_TERMINALS_IN_MARKET);
        return id;
    }

    public int getRandomMarketId () throws Exception {
        int id = Util._getRandFromRange(1, ConfDb.COUNT_OF_MARKETS_IN_REGION);
        return id;
    }

    public int getPiecesCount() throws Exception {
        int count = Util._getRandFromRange(1, ConfDb.COUNT_OF_PIECES_IN_BASKET);
        return count;
    }

    public int getRandomCountPiecesOfProduct () throws Exception {

        int idx = Util._getRandFromRange(1, ConfDb.COUNT_OF_PIECES_IN_BASKET);
        return idx;
    }
    public int getRandomProductCategryID () throws Exception {

        int product_category_id = Util._getRandFromRange(1, ConfDb.COUNT_OF_PRODUCT_CATEGORIES);
        return product_category_id;
    }
    public int getRandomCost() throws Exception {
        int cost = Util._getRandFromRange(ConfDb.MIN_COST_OF_PRODUCT, ConfDb.MAX_COST_OF_PRODUCT);
        return cost;
    }

    public java.sql.Date getRandomBirstday(int startYear, int uptoYear) throws ParseException, Exception {

        java.sql.Date date = Util._getRandomYear(startYear, uptoYear);
        return date;
    }
}

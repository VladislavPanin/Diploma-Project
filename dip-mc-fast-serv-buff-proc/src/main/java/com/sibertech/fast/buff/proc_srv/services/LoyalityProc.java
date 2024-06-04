package com.sibertech.fast.buff.proc_srv.services;

import com.sibertech.lib.DTO.ProductBasket;
import com.sibertech.lib.conf.ConfApp;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoyalityProc {

    protected LoyalitVals loyalitVals = new LoyalitVals();
    protected static Logger logback = LoggerFactory.getLogger(LoyalityProc.class);

    protected HashSet<Integer /*client_id*/> setUpdatedClients = null;

    protected Map<Integer /*region_id*/, Double  /*min_coeff*/> mapMinCoeff = null;
    protected Map<Integer /*client_id*/, Integer /*region_id*/> mapClient_by_region = null;
    protected Map<Integer /*client_id*/, Integer /*purchases summ*/> mapPurchasesSumm = null;

    public void prepere(Connection conn) throws SQLException {

        setUpdatedClients = new HashSet<>();

        mapClient_by_region = loyalitVals.getMapClient_by_region(conn);
        mapMinCoeff         = loyalitVals.getMapMinCoeff_by_region(conn);
        mapPurchasesSumm    = loyalitVals.getMapPurchasesSumm_by_client(conn);
    }
    // -------------------------------------------------------------------------

    public void collectLoyality(ProductBasket basket) {

        int client_id = basket.getClient_id();
        int purch_sum = basket.getBasket_price();

        setUpdatedClients.add(client_id);
        mapPurchasesSumm.merge(client_id, purch_sum, Integer::sum);
    }
    // -------------------------------------------------------------------------

    public void updateLoyality(Connection conn) throws SQLException {

        if(setUpdatedClients.isEmpty()) {
            logback.info(String.format("%s Обновления бонусов не проведено - не сформированы данные для обновления бонусов", ConfApp.LOG_PEFIX));
            return;
        }

        try {
            updateLoyality_intenral(conn);
            int affectedBonusRows = setUpdatedClients.size();
            //logback.info(String.format("%s Изменены бонусы %d клиентов в таблице clients", ConfApp.LOG_PEFIX, affectedBonusRows));
        }
        catch(Exception exc)
        {
            String msg = exc.getMessage();
            logback.error(String.format("%s LoyalityProc.updateLoyality(): %s", ConfApp.LOG_PEFIX_EXCEPT, msg));
        }
    }

    protected void updateLoyality_intenral(Connection conn) throws SQLException {

        int processedClientsCount = 0;
        int processStep = 100000; // количество клиентов, обрабатываемых за одну иттерацию. Не больше 1 Мб.
        // вообще говоря, длина SQL-запроса современного jdbc/Postgres разрешена до 1GB.
        // но при операциях с такой строкой требуется дополнительная память - в разы большая.
        // в результате - вылет JVM по out of memory.
        // Поэтому update бонусов будем отправлять пакетами по 10 000 клиентов - это строка запроса что-то в районе 1 Мб, заведомо меньше критического.
        String updateData = "";
        String lastUpdateData = updateData;

        for(Integer client_id: setUpdatedClients) {

            int region_id = mapClient_by_region.get(client_id);
            double  coeff = mapMinCoeff.get(region_id);
            int purch_smm = mapPurchasesSumm.get(client_id);

            long bonuses = (long)(coeff * (double)purch_smm);

            updateData += String.format("(%d, %d),", client_id, bonuses);
            processedClientsCount++;
            lastUpdateData = updateData;

            if (processedClientsCount < processStep)
                continue;

            updateLoyalityTable(conn, updateData);
            processedClientsCount = 0;
            updateData = "";
        }

        if(lastUpdateData.isEmpty())
            return;

        updateLoyalityTable(conn, lastUpdateData);
    }
    // -------------------------------------------------------------------------

    protected void updateLoyalityTable(Connection conn,
                                       String lastUpdateData) throws SQLException {

        if(lastUpdateData.endsWith(",")) {
            lastUpdateData = lastUpdateData.substring(0, lastUpdateData.length() - 1);
        }

        if(lastUpdateData.isEmpty())
            return;

        String sqlOper = String.format(FAST_UPDATE_BONUSES, lastUpdateData);

        try (var pstmt = conn.prepareStatement(sqlOper)) {
            int affectedRows = pstmt.executeUpdate();
            logback.info(String.format("%s  **SQL** Выполнен оператор UPDATE на таблице clients, обновлены бонусы для %d клиентов", ConfApp.LOG_PEFIX, affectedRows));
        }
    }
    // -------------------------------------------------------------------------
private static final String FAST_UPDATE_BONUSES  =
"""
UPDATE clients SET
    count_of_bonuses = inner_table.column_bon
FROM (
        VALUES
%s
        ) AS inner_table(column_id, column_bon)
WHERE
        clients.id = inner_table.column_id;
""";
}

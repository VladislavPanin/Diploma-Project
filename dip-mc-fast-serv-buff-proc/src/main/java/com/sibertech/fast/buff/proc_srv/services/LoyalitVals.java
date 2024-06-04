package com.sibertech.fast.buff.proc_srv.services;

import com.sibertech.lib.conf.Conf;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class LoyalitVals {

    private static final String SELECT_CLIENT_BY_REGION =
    "SELECT id, region_id FROM clients";

    public Map<Integer, Integer> getMapClient_by_region(Connection conn) throws SQLException {

        Map<Integer, Integer> mapClient_by_region = new HashMap<>();
        try (PreparedStatement preparedStatement = conn.prepareStatement(SELECT_CLIENT_BY_REGION);) {

            try(ResultSet resultSet = preparedStatement.executeQuery();) {

                while (resultSet.next()) {
                    int clientID = resultSet.getInt("id");
                    int regionID = resultSet.getInt("region_id");

                    mapClient_by_region.put(clientID, regionID);
                }
            }
        }
        return mapClient_by_region;
    }

    private static final String SELECT_MIN_COEF_FOR_REGION =
    "SELECT region_id, min(coefficient_by_region) AS  min_coeff   FROM loyalities GROUP BY region_id";

    public Map<Integer, Double> getMapMinCoeff_by_region(Connection conn) throws SQLException {

        Map<Integer, Double> mapMinCoeff_by_region = new HashMap<>();
        try (PreparedStatement preparedStatement = conn.prepareStatement(SELECT_MIN_COEF_FOR_REGION);) {

            try(ResultSet resultSet = preparedStatement.executeQuery();) {

                while (resultSet.next()) {
                    Integer regID = resultSet.getInt   ("region_id");
                    Double  coeff = resultSet.getDouble("min_coeff");

                    mapMinCoeff_by_region.put(regID, coeff);
                }
            }
        }
        return mapMinCoeff_by_region;
    }

    private static final String SELECT_PURCHASES_SUMM_FOR_CLIENT =
    "SELECT client_id, sum(price_of_one_piece * count_of_product) AS  purch_summ FROM %s GROUP BY client_id";

    public Map<Integer, Integer> getMapPurchasesSumm_by_client(Connection conn) throws SQLException {

        String table = Conf.inst().app().getSalesTableName();
        String sqlOper = String.format(SELECT_PURCHASES_SUMM_FOR_CLIENT, table);

        Map<Integer, Integer> mapPurchasesSumm_by_client = new HashMap<>();
        try (PreparedStatement preparedStatement = conn.prepareStatement(sqlOper);) {

            try(ResultSet resultSet = preparedStatement.executeQuery();) {

                while (resultSet.next()) {
                    int client_id = resultSet.getInt("client_id");
                    int purch_summ = resultSet.getInt("purch_summ");
                    mapPurchasesSumm_by_client.put(client_id, purch_summ);
                }
            }
        }
        return mapPurchasesSumm_by_client;
    }

}

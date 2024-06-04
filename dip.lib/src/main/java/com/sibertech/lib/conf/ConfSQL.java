package com.sibertech.lib.conf;

import com.sibertech.lib.DTO.Product;
import com.sibertech.lib.DTO.ProductBasket;
import com.sibertech.lib.utils.Util;

public class ConfSQL {

    public static final String DROP_DB = "DROP DATABASE %s;";
    public static final String DELETE_FROM_TABLE = "DELETE FROM %s;";
    public static final String START_TRANSACTION = "BEGIN";
    public static final String COMMIT_TRANSACTION = "COMMIT";
    public static final String ROLLBACK_TRANSACTION = "ROLLBACK";
    public static final String LOCK_TABLES_FOR_INSERT = "LOCK TABLE clients, order_numbers_dictionary, sales";

    public static final String INSERT_REGIONS    = "INSERT INTO regions(id, name, comment) VALUES (?, ?, ?);";
    public static final String INSERT_PRODUCTS   = "INSERT INTO products(id, name, category_id, cost, count_of_product, delivery_datetime, comment) VALUES (?, ?, ?, ?, ?, ?, ?);";
    public static final String INSERT_CLIENTS    = "INSERT INTO clients(id, region_id, name, surname, second_name, birthdate, count_of_bonuses, comment)VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    public static final String INSERT_MARKETS    = "INSERT INTO markets(id, region_id, market_id_on_region, name, comment) VALUES (?, ?, ?, ?, ?);";
    public static final String INSERT_TERMINALS  = "INSERT INTO terminals(id, region_id, market_id, terminal_id_on_market, comment) VALUES (?, ?, ?, ?, ?);";
    public static final String INSERT_LOYALITIES = "INSERT INTO loyalities(id, region_id, product_category_id, loyality_params_id, coefficient_by_region, comment) VALUES (?, ?, ?, ?, ?, ?)";

    public static final String INSERT_PRODUCT_CATEGIRIES = "INSERT INTO product_categories(id, name, comment) VALUES (?, ?, ?);";
    public static final String INSERT_LOYALITY_PARAMS    = "INSERT INTO loyality_params(id, name, coefficient, comment) VALUES (?, ?, ?, ?)";

    public static final String UPDATE_BONUSES        = "UPDATE clients SET count_of_bonuses = (SELECT SUM(price_of_one_piece*count_of_product/2000) AS bonus FROM sales WHERE client_id = ?) WHERE id = ?;";

    public static final String SELECT_ORDER_NUMBER   = "SELECT order_number FROM order_numbers_dictionary WHERE region_id = ? AND market_id = ? AND terminal_id = ? AND terminal_basket_number= ? AND order_time = ? AND order_date= ?;";
    public static final String SELECT_SALE_INFO      = "SELECT count_of_product, price_of_one_piece FROM sales WHERE order_number = ? AND product_id = ?;";
    // ===================================================================================================================================================================
    public static final String INSERT_SALE = """
    INSERT INTO
            %s (order_number, line_number_in_order, region_id, market_id, terminal_id, product_id, count_of_product, price_of_one_piece, order_date, client_id, comment)
    VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
    """;

    public static String get_INSERT_SALE_SQL_VALUE (String tableName, int iLineNumber, Product product, ProductBasket basket){

        // countProductPiecesInSale - количество штук продукта в этой записи sales
        String oper = String.format("[ %27s ] VALUES(%d, %d, %d, %d, %d, %d, %d, %d, '%s', %d, '%s')", tableName,

                    basket.getOrder_number(),
                    iLineNumber,
                    basket.getRegion_id(),
                    basket.getMarket_id(),
                    basket.getTerminal_id(),
                    product.getId(),
                    product.getQuantityInBasket(),
                    product.getCost(),
                    Util._to_s (basket.getSaleDate()),
                    basket.getClient_id(),
                    basket.getComment());
        return oper;
    }
    // ===================================================================================================================================================================
    public static final String INSERT_BASKET_KEY = """
    INSERT INTO
            %s (region_id, market_id, terminal_id, terminal_basket_number, basket_price, order_time, order_date, comment)
    VALUES
            (?, ?, ?, ?, ?, ?, ?, ?)
    RETURNING order_number;
    """;

    public static String get_INSERT_BASKET_KEY_SQL_VALUE (String tableName, ProductBasket basket){

        String oper = String.format("[ %27s ] VALUES(%d, %d, %d, %d, %d, '%s', '%s', '%s')", tableName,

                    basket.getRegion_id(),
                    basket.getMarket_id(),
                    basket.getTerminal_id(),
                    basket.getTerminal_basket_number(),
                    basket.getBasket_price(),
                    Util._to_s (basket.getSaleTime()),
                    Util._to_s (basket.getSaleDate()),
                    basket.getComment());
        return oper;
    }
}
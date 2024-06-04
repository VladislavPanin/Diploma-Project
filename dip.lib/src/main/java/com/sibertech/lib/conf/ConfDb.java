package com.sibertech.lib.conf;

public class ConfDb {
    public static String DB_JDBC_DRV = "org.postgresql.Driver";
    public static String PG_DB_URL   = "jdbc:postgresql://localhost:5432/postgres";        // база данных для подключения как пользователь postgres, что бы 1) точно подключится 2) иметь права на создание баз данных
    public static String PG_DB_USER  = "postgres";
    public static String PG_DB_PWD   = "postgres"; // после изменения ПЕРЕСОБРАТЬ все проекты!
    public static String BASE_DB_URL = "jdbc:postgresql://localhost:5432/"; // а этот урл - для подключения к другим БД. Их имена добавляем в конец
    // -------------------------------------------------------------------------

    public static int COUNT_OF_CLIENTS             = 100000; // 100 тысяч
    public static int COUNT_OF_REGIONS             = 10;
    public static int COUNT_OF_MARKETS_IN_REGION   = 10;
    public static int COUNT_OF_TERMINALS_IN_MARKET = 10;
    public static int COUNT_OF_BASKETS_ON_TERMINAL = 100; // Количество корзин, проданных с терминала за день.
    public static int COUNT_OF_PRODUCTS_IN_BASKET  = 10; // Количество типов товара в корзине.
    public static int COUNT_OF_PIECES_IN_BASKET    = 10; // в одной корзине может быть несколько штук товара на каждую позицию. Здесь - максимальное количество штук.
    public static int COUNT_OF_PRODUCTS            = 100000; // 100 тысяч. Количество различных товаров на складе
    public static int COUNT_OF_PRODUCT_CATEGORIES  = 1000; // количество категорий товара
    public static int COUNT_OF_LOYALITY_IN_REGION  =     COUNT_OF_PRODUCT_CATEGORIES / COUNT_OF_REGIONS;
    public static int COUNT_OF_LOYALITY_PARAMS     = 10*(COUNT_OF_LOYALITY_IN_REGION * COUNT_OF_REGIONS);
    public static int MAX_COST_OF_PRODUCT          = 99; // нужно для генерации цены товара в корзине
    public static int MIN_COST_OF_PRODUCT          = 1; // нужно для генерации цены товара в корзине
}

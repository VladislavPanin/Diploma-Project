package com.sibertech.lib.conf;


import com.sibertech.lib.DTO.Region;
import com.sibertech.lib.DTO.Loyality;
import com.sibertech.lib.DTO.Product;
import java.util.Map;
import com.sibertech.lib.DTO.LoyalityParam;
import com.sibertech.lib.DTO.ProductCategory;
import com.sibertech.lib.Gen.Gen;
import java.util.HashMap;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class DSet {
    
    protected Logger logback = LoggerFactory.getLogger(DSet.class);
    
    protected Map<Integer, Region>            regionsMap           = null;
    protected Map<Integer, Product>           productsMap          = null;
    protected Map<Integer, ProductCategory>   productCategoriesMap = null;
    protected Map<Integer, Loyality>          loyalitisMap         = null;
    protected Map<Integer, LoyalityParam>     loyalityParamsMap    = null;
        
    protected long startGeneration = System.currentTimeMillis();    
    protected int  countOfGeneratedBackets = 0;
    protected int  countOfGeneratedSales = 0;
    public void    incBaskets() {countOfGeneratedBackets++;}
    public void    incSales  () {countOfGeneratedSales++;}
    
    
    public void init () {     
        try {
            Gen gen = new Gen();
            printStartGen ();     
                    regionsMap = new HashMap<>();    

                    for (int iRegionIdx=1; iRegionIdx < ConfDb.COUNT_OF_REGIONS + 1; iRegionIdx++) {                        
                            Region reg = new Region(iRegionIdx);
                            regionsMap.put(iRegionIdx, reg);
                    }
                    gen.fillClients    (this);
                    gen.fillProducts   (this);
                    gen.fillLoyalities (this);                
                    gen.fillRegions    (this);
            printEnd (System.currentTimeMillis() - startGeneration);
        }
        catch(Exception exc) {
            String msg = exc.getMessage();
            logback.info(msg);
        }            
    }    
   
    // ------------------------------------------------------------------------------------------------------------------------
    protected void printStartGen () {        
        logback.warn(String.format("%s", ConfApp.LOG_SPLIT));
        logback.warn(String.format("%s Начата генерация полного набора данных", ConfApp.LOG_PEFIX));                
        logback.warn(String.format("%s     всего  регионов:                    %7d ", ConfApp.LOG_PEFIX, ConfDb.COUNT_OF_REGIONS));
        logback.warn(String.format("%s     магазинов в каждом  регионе:        %7d ", ConfApp.LOG_PEFIX, ConfDb.COUNT_OF_MARKETS_IN_REGION));
        logback.warn(String.format("%s     терминалов в каждом магазинов:      %7d ", ConfApp.LOG_PEFIX, ConfDb.COUNT_OF_TERMINALS_IN_MARKET));
        logback.warn(String.format("%s     чеков (корзин) на каждом терминале: %7d ", ConfApp.LOG_PEFIX, ConfDb.COUNT_OF_BASKETS_ON_TERMINAL));
        logback.warn(String.format("%s     продуктов в каждом чеке (корзине):  %7d ", ConfApp.LOG_PEFIX, ConfDb.COUNT_OF_PRODUCTS_IN_BASKET));                
    }
    
    protected void printEnd (long deltaTime) {
        logback.warn(String.format("%s", ConfApp.LOG_PEFIX));
        logback.warn(String.format("%s Генерация полного набора данных завершена. Время генерации равно %d миллисекунд", ConfApp.LOG_PEFIX, deltaTime));        
        logback.warn(String.format("%s Общее количество сгенерированных корзин %,d", ConfApp.LOG_PEFIX, countOfGeneratedBackets, countOfGeneratedBackets/1000));        
        logback.warn(String.format("%s Общее количество строк продаж товаров %,d", ConfApp.LOG_PEFIX, countOfGeneratedSales, countOfGeneratedSales/1000));
        logback.warn(String.format("%s", ConfApp.LOG_SPLIT));
    }    
}

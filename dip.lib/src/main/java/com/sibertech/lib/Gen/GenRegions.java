package com.sibertech.lib.Gen;

import com.sibertech.lib.DTO.Client;
import com.sibertech.lib.DTO.Market;
import com.sibertech.lib.DTO.Product;
import com.sibertech.lib.DTO.ProductBasket;
import com.sibertech.lib.DTO.Region;
import com.sibertech.lib.DTO.Terminal;
import com.sibertech.lib.conf.DSet;
import com.sibertech.lib.conf.ConfDb;
import com.sibertech.lib.conf.ConfApp;
import com.sibertech.lib.utils.Util;
import java.util.ArrayList;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenRegions {

    protected Logger logback = LoggerFactory.getLogger(GenRegions.class);
    
    protected NextID markID = new NextID(1);
    protected NextID termID = new NextID(1);
    // ===================================================================================================
    public class ClientIdProvider 
    {       
        protected ArrayList<Integer> clientIds = new ArrayList<>();            
        protected int currentIdxRunner = 1;
        protected int idxCount;
        
        public ClientIdProvider (Region region){
            
            Map<Integer, Client> clientsMap = region.getClientsMap();
            for (Client clnt: clientsMap.values()) 
                clientIds.add(clnt.getClient_id());            
            idxCount = clientIds.size();
        }        
        
        public int getNextClientId() {
            
            int idx = clientIds.get(currentIdxRunner++);
            if (currentIdxRunner >= idxCount) // в ArrayList индексация с 0, поэтому (currentIdxRunner >= idxCount), а не (currentIdxRunner > idxCount)
                currentIdxRunner = 0;
        
            return idx;
        }        
    }
    // ===================================================================================================
    /**
     * метод генерации данных для отдельного региона.
     * То есть - должен быть вызван для каждого региона индивидуально.
     * @param dSet   - общий набор данных для всех регионов
     * @param region - регион, который нужно наполнить данными о магазинах, терминалах, продажах
     */
    public void generate (DSet dSet, Region region) {
                 
        int iRegion = region.getId();
        
        int client_id = -1;
        
        int iMarket   = -1;
        int iTerminal = -1;
        int iBasket   = -1;          
        int iProduct;       
        int basket_price;
        
        try {
                ClientIdProvider clientIdProvider = new ClientIdProvider(region); // для каждого региона - свой экземпляр
                
                for (iMarket = 1; iMarket <= ConfDb.COUNT_OF_MARKETS_IN_REGION; iMarket++) {                    
                    Market mark = new Market(markID.next(), iMarket, iRegion, String.format("Магазин номер #%d в регионе #%d", iMarket, region.getId()));
                    region.addMarket(mark);
                    
                    for (iTerminal = 1; iTerminal <= ConfDb.COUNT_OF_TERMINALS_IN_MARKET; iTerminal++) {                        
                        Terminal term = new Terminal(termID.next(), iTerminal, iMarket, iRegion);
                        mark.addTerminal(term);
                        
                        for (iBasket = 1; iBasket <= ConfDb.COUNT_OF_BASKETS_ON_TERMINAL; iBasket++) {                            
                            ProductBasket basket = new ProductBasket();
                            term.addProductBasket(basket);
                            
                            client_id = clientIdProvider.getNextClientId();
                            
                            basket.setClient_id   (client_id);
                            basket.setRegion_id   (iRegion);
                            basket.setMarket_id   (iMarket);
                            basket.setTerminal_id (iTerminal);
                            
                            basket.setTerminal_basket_number(iBasket);
                            basket_price = 0;
                            dSet.incBaskets();
                            
                            for (iProduct = 1; iProduct <= ConfDb.COUNT_OF_PRODUCTS_IN_BASKET; iProduct++) {                            
                                
                                // корзина используется только в момент записи продажи в таблицу sales.
                                // а один и тот же экземпляр класса Product может быть использован в разных корзинах
                                // поэтому, количество конкретного продукта в корзине, и суммарная стоимость корзины 
                                // будет сгенерированы актуатором и подставлены в оператор SQL непосредственно 
                                // перед вставкой в базу данных как внешние переменные, не как поля Product/ProductBasket
                                
                                int randomProductID = Util._getRandFromRange(1, ConfDb.COUNT_OF_PRODUCTS);        
                                
                                Product prod = dSet.getProductsMap().get(randomProductID); // случайный продукт                       
                                basket.addProduct(prod);                                
                                dSet.incSales();
                            }
                            basket.setBasket_price(basket_price);
                        }                        
                    }                    
                }   
                region.setFillCompliteded();
        }
        catch (Exception exc) {
            logback.warn(String.format("%s GenRegions::generate(): client_id= %d; iRegion= %d  iMarket= %d  iTerminal= %d iBasket= %d; msg ---> %s", ConfApp.LOG_PEFIX_EXCEPT, client_id, iRegion, iMarket, iTerminal, iBasket, exc.getMessage()));            
        }
    }
}

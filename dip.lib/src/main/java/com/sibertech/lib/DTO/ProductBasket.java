package com.sibertech.lib.DTO;

import com.sibertech.lib.conf.ConfDb;
import lombok.Data;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductBasket {
    
    protected Map<Integer, Product> basket = new HashMap<>();
    
    protected int region_id;
    protected int market_id;
    protected int terminal_id;
    protected int client_id; 
    protected int terminal_basket_number; //Порядковый номер чека, на конкретном терминале
    
    protected java.sql.Time saleTime = null;       // обновляется в актуаторе при вставке в таблицу. Время продажи
    protected java.sql.Date saleDate = null;       // обновляется в актуаторе при вставке в таблицу. Дата продажи
    protected int basket_price = -1;      // обновляется в актуаторе при вставке в таблицу. Стоимость всего чека
    
    protected int order_number = -1;      // будет получен непосредственно перед вставкой. Номер записи в таблицу order_numbers_dictionary
    protected String comment = "Стандартный комментарий корзины для sale";
        
    public void addProduct(Product product){
        int id = basket.size()+1;
        basket.put(id,product);
    }
    
    public void setEqual (ProductBasket param)  {
        // нужен при вставке в БД
        
        // текущую продуктовую корзину делаем равной той, которую передали параметром
        // число продуктов в корзинах уже должно быть равно
        for (int i=1; i <= ConfDb.COUNT_OF_PRODUCTS_IN_BASKET; i++) {
         
            Product this_prod = this.basket.get(i);
            Product param_prod = param.basket.get(i);            
            this_prod.setEqual(param_prod);
        }        
        // приравниваем только те параметры, которые во первых - примитивы, а во вторых - не будут меняться при модификации при вставке        						
        this. region_id               = param. region_id;
        this. market_id               = param. market_id;
        this. terminal_id             = param. terminal_id;
        this. client_id               = param. client_id;
        this. terminal_basket_number  = param. terminal_basket_number;
    }
}

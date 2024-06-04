package com.sibertech.lib.DTO;

import com.sibertech.lib.utils.Util;
import lombok.Data;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    
    protected int id = -1;
    protected String name = "not inited Product";
    
    protected int cost = -1;
    protected int countOnStore = -1;    
    protected int quantityInBasket = -1;    
    protected int category_id = -1;
    protected String comment = "not inited comment of Product";
    protected Timestamp delivery = null;
    
    public Product (int id, int cost, int category_id) {
        this.id = id;
        this.name = "Продукт #" + id;
        this.comment = "Комментарий для продукта #" + id;
        
        this.category_id = category_id;
        
        this.cost = cost;
        this.countOnStore = 10000000; // 10 mlns
        this.delivery = Util._nowTimestamp();
    }    
    
     public void setEqual (Product product) {
        this.id = product.id;
        this.name = product.name;
        this.comment = product.comment;
        
        this.category_id = product.category_id;
        
        this.cost = product.cost;
        this.countOnStore = product.countOnStore; 
        this.delivery = product.delivery;
     }
}

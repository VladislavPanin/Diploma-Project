package com.sibertech.lib.Gen;

import com.sibertech.lib.DTO.Product;
import com.sibertech.lib.DTO.ProductCategory;
import com.sibertech.lib.db.actuator.DtoActuator;
import com.sibertech.lib.conf.DSet;
import com.sibertech.lib.conf.ConfDb;
import java.util.HashMap;
import java.util.Map;

public class GenAuxProducts {
    
    protected DtoActuator actuator = new DtoActuator();
    
    public void generate (DSet dSet) throws Exception {
        
        dSet.setProductCategoriesMap(this.genProductCategories ());
        dSet.setProductsMap (genProducts (dSet));        
    }
    // --------------------------------------------------------------------
    protected Map<Integer, ProductCategory> genProductCategories () {
        
        Map<Integer, ProductCategory> categs = new HashMap<>();        
        for(int i=1; i <= ConfDb.COUNT_OF_PRODUCT_CATEGORIES; i++) {
            
            ProductCategory cat = new ProductCategory(i);
            categs.put(i, cat);
        }        
        return categs;
    }    
    
    protected Map<Integer, Product> genProducts (DSet dSet) throws Exception {
        
        Map<Integer, Product> prods = new HashMap<>();
        for(int i=1; i <= ConfDb.COUNT_OF_PRODUCTS; i++) {
        
            int product_category_id = actuator.getRandomProductCategryID();
            
            Product prod = new Product(i, actuator.getRandomCost(), product_category_id);
            prods.put(i, prod);
        }        
        return prods;
    }
}

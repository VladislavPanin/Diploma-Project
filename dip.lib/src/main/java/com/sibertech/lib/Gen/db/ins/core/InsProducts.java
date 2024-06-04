package com.sibertech.lib.Gen.db.ins.core;

import com.sibertech.lib.DTO.Product;
import com.sibertech.lib.conf.DSet;
import com.sibertech.lib.conf.ConfSQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class InsProducts  extends Ins {
    
    @Override
    public void insert_internal (DSet dSet, Connection conn) throws SQLException {
        
        Map<Integer, Product> productsMap = dSet.getProductsMap();
        Product product;
//"INSERT INTO products(id, name, category_id, cost, count_of_product, delivery_datetime, comment) VALUES (?, ?, ?, ?, ?, ?, ?);";            
        try (PreparedStatement preparedStatement = conn.prepareStatement(ConfSQL.INSERT_PRODUCTS);) {
                
                for(Map.Entry<Integer, Product> entry : productsMap.entrySet()) {
                    product = entry.getValue();    
                    
                    preparedStatement.setInt      (1, product.getId());
                    preparedStatement.setString   (2, product.getName());                    
                    preparedStatement.setInt      (3, product.getCategory_id());
                    preparedStatement.setInt      (4, product.getCost());
                    preparedStatement.setInt      (5, product.getCountOnStore());
                    preparedStatement.setTimestamp(6, product.getDelivery());            
                    preparedStatement.setString   (7, product.getComment());

                    preparedStatement.execute();
                    this.incLineCount();
                }
            }        
    }
}

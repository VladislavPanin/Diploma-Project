package com.sibertech.lib.Gen.db.ins.core;

import com.sibertech.lib.DTO.ProductCategory;
import com.sibertech.lib.conf.DSet;
import com.sibertech.lib.conf.ConfSQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class InsProductCategories  extends Ins {
    
    @Override
    public void insert_internal (DSet dSet, Connection conn) throws SQLException {
        
        Map<Integer, ProductCategory> productCategoriesMap = dSet.getProductCategoriesMap();
        ProductCategory productCategory;
        try (PreparedStatement preparedStatement = conn.prepareStatement(ConfSQL.INSERT_PRODUCT_CATEGIRIES);) {
                
                for(Map.Entry<Integer, ProductCategory> entry : productCategoriesMap.entrySet()) {
                    productCategory = entry.getValue();            

                    preparedStatement.setInt      (1, productCategory.getId());
                    preparedStatement.setString   (2, productCategory.getName());
                    preparedStatement.setString   (3, productCategory.getComment());

                    preparedStatement.execute();                      
                    this.incLineCount();
                }
        }
    }   
}

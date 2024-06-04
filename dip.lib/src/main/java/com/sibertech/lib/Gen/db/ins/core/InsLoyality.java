package com.sibertech.lib.Gen.db.ins.core;

import com.sibertech.lib.DTO.Loyality;
import com.sibertech.lib.conf.DSet;
import com.sibertech.lib.conf.ConfSQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class InsLoyality extends Ins {
    
    @Override
    public void insert_internal (DSet dSet, Connection conn) throws SQLException {
        
        Map<Integer, Loyality> loyalitiesMap = dSet.getLoyalitisMap();
        
        try (PreparedStatement preparedStatement = conn.prepareStatement(ConfSQL.INSERT_LOYALITIES);) {
                
                for(Map.Entry<Integer, Loyality> entry : loyalitiesMap.entrySet()) {
                    Loyality loyality = entry.getValue();    
                    
                    preparedStatement.setInt      (1, loyality.getId());                    
                    preparedStatement.setInt      (2, loyality.getRegion_id());
                    preparedStatement.setInt      (3, loyality.getCategory_id());
                    preparedStatement.setInt      (4, loyality.getLoyality_params_id());            
                    preparedStatement.setDouble   (5, loyality.getCoefficient_by_region());
                    preparedStatement.setString   (6, loyality.getComment());

                    preparedStatement.execute();
                    this.incLineCount();
                }
            }        
    }
}

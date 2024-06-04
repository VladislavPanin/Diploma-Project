package com.sibertech.lib.Gen.db.ins.core;

import com.sibertech.lib.DTO.LoyalityParam;
import com.sibertech.lib.conf.DSet;
import com.sibertech.lib.conf.ConfApp;
import com.sibertech.lib.conf.ConfSQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class InsLoyalityParams extends Ins {
    
    @Override
    public void insert_internal (DSet dSet, Connection conn) throws SQLException {
        
        Map<Integer, LoyalityParam> loyalityParamsMap = dSet.getLoyalityParamsMap();        
        try (PreparedStatement preparedStatement = conn.prepareStatement(ConfSQL.INSERT_LOYALITY_PARAMS);) {
                
                for(Map.Entry<Integer, LoyalityParam> entry : loyalityParamsMap.entrySet()) {
                    LoyalityParam loyalityParam = entry.getValue();            

                    preparedStatement.setInt      (1, loyalityParam.getId());
                    preparedStatement.setString   (2, loyalityParam.getName());
                    preparedStatement.setDouble   (3, loyalityParam.getCoefficient());
                    preparedStatement.setString   (4, loyalityParam.getComment());

                    preparedStatement.execute();                                        
                    this.incLineCount();
                }
        }
        catch (Exception exc)
        {
            String msg = exc.getMessage();
            logback.warn(String.format("%s InsLoyalityParams::insert_internal(): %s", ConfApp.LOG_PEFIX_EXCEPT, msg));
        }
    }
}

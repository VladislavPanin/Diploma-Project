package com.sibertech.lib.Gen.db.ins.core;

import com.sibertech.lib.DTO.Region;
import com.sibertech.lib.conf.DSet;
import com.sibertech.lib.conf.ConfSQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class InsRegions extends Ins {
    
    @Override
    protected void insert_internal(DSet dSet, Connection conn)  throws SQLException {    
        
        Region region;        
        try (PreparedStatement preparedStatement = conn.prepareStatement(ConfSQL.INSERT_REGIONS);) {

            for(Map.Entry<Integer, Region> entry : dSet.getRegionsMap().entrySet()) {
                region = entry.getValue();            

                preparedStatement.setInt      (1, region.getId());
                preparedStatement.setString   (2, region.getName());
                preparedStatement.setString   (3, "some comment for Region #" + region.getId());

                preparedStatement.execute();
                this.incLineCount();
            }
        }       
    }
}

package com.sibertech.lib.db;

import com.sibertech.lib.conf.ConfApp;
import com.sibertech.lib.conf.ConfDb;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DB {
    
    protected Connection conn = null;    
    protected Logger logbackDB = LoggerFactory.getLogger(DB.class);

    protected void init (String db_url, String db_user, String db_passw) throws SQLException, ClassNotFoundException {
        try {
            if (conn != null)
                conn.close();

            Class.forName(ConfDb.DB_JDBC_DRV);
            conn = DriverManager.getConnection(db_url, db_user, db_passw);
        }
        catch (Exception exc)
        {
            String err = exc.getMessage();
            logbackDB.error(String.format("%s  DB: ConfDb: %s", ConfApp.LOG_PEFIX_EXCEPT, err));
        }
    }
    
    protected void close() throws SQLException {
        if (conn != null)
             conn.close();
        conn = null;
        logbackDB.warn(String.format("%s  DB: соединение закрыто ", ConfApp.LOG_PEFIX));
    }
}
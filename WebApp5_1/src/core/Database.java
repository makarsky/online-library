package core;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class Database {
    
    private static Connection conn;
    private static InitialContext initialContext;
    private static Context context;
    private static DataSource ds;
    
    public static Connection getConnection() {
        
        try {
            initialContext = new InitialContext();
            context = (Context) initialContext.lookup("java:comp/env");
            
            ds = (DataSource)context.lookup("jdbc/library");
            if (conn == null) {
                conn = ds.getConnection();
            }
        } catch (NamingException | SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
        return conn;
    }
}

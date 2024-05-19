package hellofx;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Connect {
    /**
     * Connect to a sample database
     */
    public static void connect() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            // db parameters
            String url = "jdbc:sqlite:chinook.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            // Método 1: Usando ResultSet para obtener el nombre de las tablas
            System.out.println("Método 1: Usando ResultSet");
            Statement stmt1 = conn.createStatement();
            ResultSet rs1 = stmt1.executeQuery("SELECT name FROM sqlite_master WHERE type='table'");
            while (rs1.next()) {
                String tableName = rs1.getString("name");
                System.out.println("Table: " + tableName);
            }
            rs1.close();
            stmt1.close();
            
            // Método 2: Usando DatabaseMetaData para obtener el nombre de las tablas
            System.out.println("\nMétodo 2: Usando DatabaseMetaData");
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs2 = metaData.getTables(null, null, null, new String[]{"TABLE"});
            while (rs2.next()) {
                String tableName = rs2.getString("TABLE_NAME");
                System.out.println("Table: " + tableName);
            }
            rs2.close();
            
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    public static void main(String[] args) {
        connect();
    }
}

package hellofx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {

    public static void main(String[] args) {


        String jdbcUrl = "jdbc:mysql://localhost:8080/Electricity_Manager";
        String user = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(jdbcUrl, user, password)) {
                if (connection != null) {
                    System.out.println("Connected to MySQL database successfully!");
                } else {
                    System.out.println("Failed to establish a database connection.");
                }
            } 
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

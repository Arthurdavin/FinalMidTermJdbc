//package co.istad.config;

//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public class DbConfig {
//    private static Connection conn;
//    public static Connection getInstance() {
//        return conn;
//    }
//    public static void init() {
//        if (conn == null) {
//            try {
//                Class.forName("org.postgresql.Driver");
//            } catch (ClassNotFoundException e) {
//                System.out.println("driver load failed: " + e.getMessage());
//            }
//            final String URL = "jdbc:postgresql://localhost:5432/hospital";
//            final String USER = "postgres";
//            final String PASSWORD = "vin1205";
//            try {
//                conn = DriverManager.getConnection(URL, USER, PASSWORD);
//            } catch (SQLException e) {
//                System.out.println("Error SQL: " + e.getMessage());
//            }
//        } else {
//            System.out.println("Connection is already initialize");
//        }
//    }
//}

package co.istad.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConfig {

    private static final String URL = "jdbc:postgresql://localhost:5432/hospital";
    private static final String USER = "postgres";
    private static final String PASSWORD = "vin1205";

    private DbConfig() {}

    public static Connection getInstance() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("❌ Cannot connect to database", e);
        }
    }
}

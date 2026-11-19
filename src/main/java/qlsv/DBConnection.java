package qlsv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Lớp quản lý kết nối MySQL.
 * Có thể cấu hình tài khoản bằng biến môi trường DB_USER và DB_PASSWORD.
 */
public final class DBConnection {
    private static final String URL =
            "jdbc:mysql://localhost:3306/quanghieu"
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Ho_Chi_Minh"
            + "&characterEncoding=UTF-8";

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        String user = System.getenv().getOrDefault("DB_USER", "root");
        String password = System.getenv().getOrDefault("DB_PASSWORD", "");
        return DriverManager.getConnection(URL, user, password);
    }
}

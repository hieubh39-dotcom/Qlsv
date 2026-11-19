package qlsv;

import javax.swing.*;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Kiểm tra kết nối trước khi mở giao diện
                try (Connection ignored = DBConnection.getConnection()) {
                    System.out.println("Ket noi MySQL thanh cong!");
                }
                MainFrame.applyTheme();
                new MainFrame().setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Không thể kết nối MySQL. Hãy kiểm tra:\n"
                        + "1. MySQL đang chạy.\n"
                        + "2. Đã chạy file SQL tạo database.\n"
                        + "3. Tài khoản/mật khẩu trong DBConnection đúng.\n\n"
                        + "Chi tiết: " + ex.getMessage(),
                        "Lỗi kết nối cơ sở dữ liệu", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

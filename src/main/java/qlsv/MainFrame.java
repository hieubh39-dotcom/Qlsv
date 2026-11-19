package qlsv;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {
    private static final Color NAVY = new Color(25, 48, 78);
    private static final Color BLUE = new Color(45, 112, 220);
    private static final Color MUTED = new Color(112, 126, 145);

    public MainFrame() {
        setTitle("Phần mềm quản lý sinh viên");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1240, 820);
        setMinimumSize(new Dimension(1050, 700));
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245, 247, 251));
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(new EmptyBorder(18, 26, 18, 26));

        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        brand.setOpaque(false);
        JLabel logo = new JLabel("SV");
        logo.setOpaque(true);
        logo.setBackground(new Color(67, 130, 230));
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        logo.setPreferredSize(new Dimension(44, 44));

        JPanel brandText = new JPanel();
        brandText.setOpaque(false);
        brandText.setLayout(new BoxLayout(brandText, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("HỆ THỐNG QUẢN LÝ SINH VIÊN");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        JLabel sub = new JLabel("QUẢN LÝ SINH VIÊN • GIẢNG VIÊN • MÔN HỌC • ĐIỂM");
        sub.setForeground(new Color(194, 208, 226));
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        brandText.add(title);
        brandText.add(Box.createVerticalStrut(4));
        brandText.add(sub);
        brand.add(logo);
        brand.add(brandText);

        JLabel status = new JLabel("●  HỆ THỐNG QUẢN LÝ");
        status.setForeground(new Color(177, 231, 204));
        status.setFont(new Font("Segoe UI", Font.BOLD, 11));
        header.add(brand, BorderLayout.WEST);
        header.add(status, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setTabPlacement(JTabbedPane.TOP);
        tabs.setBorder(new EmptyBorder(8, 12, 10, 12));

        tabs.addTab("  Sinh viên  ", new CrudPanel("QUẢN LÝ SINH VIÊN",
                "sinh_vien", "ma_sv",
                new ColumnInfo("ma_sv", "Mã sinh viên", ColumnInfo.DataType.STRING),
                new ColumnInfo("ho_ten", "Họ và tên", ColumnInfo.DataType.STRING),
                new ColumnInfo("ngay_sinh", "Ngày sinh (yyyy-MM-dd)", ColumnInfo.DataType.DATE),
                new ColumnInfo("gioi_tinh", "Giới tính", ColumnInfo.DataType.STRING),
                new ColumnInfo("email", "Email", ColumnInfo.DataType.STRING),
                new ColumnInfo("so_dien_thoai", "Số điện thoại", ColumnInfo.DataType.STRING),
                new ColumnInfo("dia_chi", "Địa chỉ", ColumnInfo.DataType.STRING)));

        tabs.addTab("  Giảng viên  ", new CrudPanel("QUẢN LÝ GIẢNG VIÊN",
                "giang_vien", "ma_gv",
                new ColumnInfo("ma_gv", "Mã giảng viên", ColumnInfo.DataType.STRING),
                new ColumnInfo("ho_ten", "Họ và tên", ColumnInfo.DataType.STRING),
                new ColumnInfo("email", "Email", ColumnInfo.DataType.STRING),
                new ColumnInfo("so_dien_thoai", "Số điện thoại", ColumnInfo.DataType.STRING),
                new ColumnInfo("khoa", "Khoa", ColumnInfo.DataType.STRING)));

        tabs.addTab("  Môn học  ", new CrudPanel("QUẢN LÝ MÔN HỌC",
                "mon_hoc", "ma_mh",
                new ColumnInfo("ma_mh", "Mã môn học", ColumnInfo.DataType.STRING),
                new ColumnInfo("ten_mh", "Tên môn học", ColumnInfo.DataType.STRING),
                new ColumnInfo("so_tin_chi", "Số tín chỉ", ColumnInfo.DataType.INTEGER),
                new ColumnInfo("ma_gv", "Mã giảng viên (có thể trống)", ColumnInfo.DataType.STRING)));

        tabs.addTab("  Điểm  ", new CrudPanel("QUẢN LÝ ĐIỂM",
                "diem", "ma_diem",
                new ColumnInfo("ma_diem", "Mã điểm (để trống khi thêm)", ColumnInfo.DataType.INTEGER),
                new ColumnInfo("ma_sv", "Mã sinh viên", ColumnInfo.DataType.STRING),
                new ColumnInfo("ma_mh", "Mã môn học", ColumnInfo.DataType.STRING),
                new ColumnInfo("diem_qua_trinh", "Điểm quá trình (0-10)", ColumnInfo.DataType.DECIMAL),
                new ColumnInfo("diem_thi", "Điểm thi (0-10)", ColumnInfo.DataType.DECIMAL)));

        add(tabs, BorderLayout.CENTER);

        JLabel footer = new JLabel("  ĐỒ ÁN JAVA SWING + MYSQL     |     QUẢN LÝ ĐÀO TẠO");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(MUTED);
        footer.setBorder(new EmptyBorder(5, 18, 12, 18));
        add(footer, BorderLayout.SOUTH);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Hệ thống");
        JMenuItem about = new JMenuItem("Thông tin phần mềm");
        JMenuItem exit = new JMenuItem("Thoát");
        about.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Phần mềm Quản lý sinh viên\nJava Swing + MySQL\nĐồ án môn học"));
        exit.addActionListener(e -> dispose());
        menu.add(about);
        menu.addSeparator();
        menu.add(exit);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    public static void applyTheme() {
        try {
            FlatLightLaf.setup();
            UIManager.put("Component.arc", 10);
            UIManager.put("Button.arc", 10);
            UIManager.put("TextComponent.arc", 8);
            UIManager.put("TabbedPane.selectedBackground", Color.WHITE);
            UIManager.put("TabbedPane.underlineColor", BLUE);
            UIManager.put("TabbedPane.showTabSeparators", true);
            UIManager.put("Table.selectionBackground", new Color(219, 234, 254));
            UIManager.put("Table.selectionForeground", NAVY);
            UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 13));
        } catch (Exception ex) {
            System.err.println("Không thể áp dụng giao diện FlatLaf: " + ex.getMessage());
        }
    }
}

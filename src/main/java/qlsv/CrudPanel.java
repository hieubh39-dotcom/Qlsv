package qlsv;

import qlsv.ColumnInfo.DataType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.math.BigDecimal;

public class CrudPanel extends JPanel {
    private static final Color NAVY = new Color(25, 48, 78);
    private static final Color BLUE = new Color(45, 112, 220);
    private static final Color GREEN = new Color(22, 163, 110);
    private static final Color RED = new Color(220, 70, 80);
    private static final Color TEXT = new Color(48, 61, 78);
    private static final Color MUTED = new Color(112, 126, 145);
    private static final Color BORDER = new Color(225, 231, 239);

    private final String tableName;
    private final String primaryKey;
    private final ColumnInfo[] columns;
    private final JTextField[] inputs;
    private final JTextField searchField = new JTextField(24);
    private final JTable table = new JTable();
    private final DefaultTableModel model;
    private Object selectedKey;

    public CrudPanel(String title, String tableName, String primaryKey, ColumnInfo... columns) {
        this.tableName = tableName;
        this.primaryKey = primaryKey;
        this.columns = columns;
        this.inputs = new JTextField[columns.length];

        setLayout(new BorderLayout(0, 16));
        setBackground(new Color(245, 247, 251));
        setBorder(new EmptyBorder(20, 22, 20, 22));

        JPanel titleArea = new JPanel(new BorderLayout());
        titleArea.setOpaque(false);
        JLabel heading = new JLabel(title);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 22));
        heading.setForeground(NAVY);
        JLabel subtitle = new JLabel("Quản lý, cập nhật và tra cứu dữ liệu trong hệ thống");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(MUTED);

        JPanel titleText = new JPanel();
        titleText.setOpaque(false);
        titleText.setLayout(new BoxLayout(titleText, BoxLayout.Y_AXIS));
        titleText.add(heading);
        titleText.add(Box.createVerticalStrut(5));
        titleText.add(subtitle);
        titleArea.add(titleText, BorderLayout.WEST);
        add(titleArea, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 14));
        content.setOpaque(false);

        JPanel formCard = createCard();
        formCard.setLayout(new BorderLayout(0, 12));
        JLabel formTitle = sectionTitle("THÔNG TIN");
        formCard.add(formTitle, BorderLayout.NORTH);

        JPanel fields = new JPanel(new GridBagLayout());
        fields.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 7, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        int rows = (columns.length + 1) / 2;
        for (int i = 0; i < columns.length; i++) {
            int row = i / 2;
            int col = (i % 2) * 2;

            JLabel label = new JLabel(columns[i].label());
            label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            label.setForeground(TEXT);

            inputs[i] = new JTextField();
            inputs[i].setFont(new Font("Segoe UI", Font.PLAIN, 13));
            inputs[i].putClientProperty("JTextField.placeholderText", "Nhập " + columns[i].label().toLowerCase());

            gbc.gridx = col;
            gbc.gridy = row;
            gbc.weightx = 0;
            gbc.gridwidth = 1;
            fields.add(label, gbc);

            gbc.gridx = col + 1;
            gbc.weightx = 1;
            fields.add(inputs[i], gbc);
        }

        JPanel formBottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        formBottom.setOpaque(false);
        JButton addButton = actionButton("＋  Thêm mới", GREEN);
        JButton updateButton = actionButton("✎  Cập nhật", BLUE);
        JButton deleteButton = actionButton("✕  Xóa", RED);
        JButton clearButton = actionButton("Làm trống", new Color(105, 119, 139));
        formBottom.add(addButton);
        formBottom.add(updateButton);
        formBottom.add(deleteButton);
        formBottom.add(clearButton);

        formCard.add(fields, BorderLayout.CENTER);
        formCard.add(formBottom, BorderLayout.SOUTH);

        JPanel dataCard = createCard();
        dataCard.setLayout(new BorderLayout(0, 12));

        JPanel dataHeader = new JPanel(new BorderLayout());
        dataHeader.setOpaque(false);
        dataHeader.add(sectionTitle("DANH SÁCH DỮ LIỆU"), BorderLayout.WEST);

        JPanel searchTools = new JPanel(new FlowLayout(FlowLayout.RIGHT, 7, 0));
        searchTools.setOpaque(false);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.putClientProperty("JTextField.placeholderText", "Nhập từ khóa tìm kiếm...");
        JButton searchButton = actionButton("Tìm kiếm", BLUE);
        JButton showAllButton = actionButton("Tải lại", new Color(105, 119, 139));
        searchTools.add(searchField);
        searchTools.add(searchButton);
        searchTools.add(showAllButton);
        dataHeader.add(searchTools, BorderLayout.EAST);

        model = new DefaultTableModel(columnLabels(), 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setForeground(TEXT);
        table.setGridColor(new Color(235, 239, 245));
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setForeground(NAVY);
        table.getTableHeader().setBackground(new Color(238, 243, 250));
        table.getTableHeader().setPreferredSize(new Dimension(0, 36));
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(NAVY);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                        tbl, value, isSelected, hasFocus, row, column);
                if (!isSelected) c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(249, 251, 254));
                setBorder(new EmptyBorder(0, 9, 0, 9));
                return c;
            }
        };
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        scroll.getViewport().setBackground(Color.WHITE);

        dataCard.add(dataHeader, BorderLayout.NORTH);
        dataCard.add(scroll, BorderLayout.CENTER);

        content.add(formCard, BorderLayout.NORTH);
        content.add(dataCard, BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);

        addButton.addActionListener(e -> insertRecord());
        updateButton.addActionListener(e -> updateRecord());
        deleteButton.addActionListener(e -> deleteRecord());
        clearButton.addActionListener(e -> clearInputs());
        searchButton.addActionListener(e -> loadData(searchField.getText().trim()));
        searchField.addActionListener(e -> loadData(searchField.getText().trim()));
        showAllButton.addActionListener(e -> {
            searchField.setText("");
            loadData("");
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int row = table.convertRowIndexToModel(table.getSelectedRow());
                for (int i = 0; i < columns.length; i++) {
                    Object value = model.getValueAt(row, i);
                    inputs[i].setText(value == null ? "" : value.toString());
                }
                selectedKey = model.getValueAt(row, indexOfPrimaryKey());
            }
        });

        loadData("");
    }

    private JPanel createCard() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(14, 16, 14, 16)));
        return panel;
    }

    private JLabel sectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(NAVY);
        return label;
    }

    private JButton actionButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(9, 15, 9, 15));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private Object[] columnLabels() {
        Object[] labels = new Object[columns.length];
        for (int i = 0; i < columns.length; i++) labels[i] = columns[i].label();
        return labels;
    }

    private int indexOfPrimaryKey() {
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].name().equals(primaryKey)) return i;
        }
        throw new IllegalStateException("Không tìm thấy khóa chính " + primaryKey);
    }

    private Object parseInput(int index) {
        String value = inputs[index].getText().trim();
        if (value.isEmpty()) return null;
        try {
            return switch (columns[index].type()) {
                case STRING -> value;
                case INTEGER -> Integer.valueOf(value);
                case DECIMAL -> new BigDecimal(value);
                case DATE -> Date.valueOf(value);
            };
        } catch (Exception ex) {
            throw new IllegalArgumentException("Giá trị không hợp lệ ở trường "
                    + columns[index].label()
                    + ". Ngày nhập yyyy-MM-dd; số thập phân dùng dấu chấm.");
        }
    }

    private Object[] readInputs() {
        Object[] values = new Object[columns.length];
        for (int i = 0; i < columns.length; i++) values[i] = parseInput(i);
        if (values[indexOfPrimaryKey()] == null && !primaryKey.equals("ma_diem")) {
            throw new IllegalArgumentException("Mã định danh không được để trống.");
        }
        return values;
    }

    private void insertRecord() {
        try {
            Object[] values = readInputs();
            StringBuilder sql = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
            for (int i = 0; i < columns.length; i++) {
                if (i > 0) sql.append(",");
                sql.append(columns[i].name());
            }
            sql.append(") VALUES (").append("?,".repeat(columns.length));
            sql.setLength(sql.length() - 1);
            sql.append(")");

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql.toString())) {
                for (int i = 0; i < values.length; i++) ps.setObject(i + 1, values[i]);
                ps.executeUpdate();
            }
            JOptionPane.showMessageDialog(this, "Thêm dữ liệu thành công!");
            clearInputs();
            loadData("");
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void updateRecord() {
        if (selectedKey == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng trong bảng để sửa.");
            return;
        }
        try {
            Object[] values = readInputs();
            StringBuilder sql = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
            boolean first = true;
            for (ColumnInfo column : columns) {
                if (column.name().equals(primaryKey)) continue;
                if (!first) sql.append(",");
                sql.append(column.name()).append("=?");
                first = false;
            }
            sql.append(" WHERE ").append(primaryKey).append("=?");

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql.toString())) {
                int parameter = 1;
                for (int i = 0; i < columns.length; i++) {
                    if (!columns[i].name().equals(primaryKey)) ps.setObject(parameter++, values[i]);
                }
                ps.setObject(parameter, selectedKey);
                if (ps.executeUpdate() == 0) throw new SQLException("Không tìm thấy bản ghi cần sửa.");
            }
            JOptionPane.showMessageDialog(this, "Cập nhật dữ liệu thành công!");
            clearInputs();
            loadData("");
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void deleteRecord() {
        if (selectedKey == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng trong bảng để xóa.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa bản ghi đang chọn?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM " + tableName + " WHERE " + primaryKey + "=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, selectedKey);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Xóa dữ liệu thành công!");
            clearInputs();
            loadData("");
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void loadData(String keyword) {
        model.setRowCount(0);
        StringBuilder sql = new StringBuilder("SELECT ");
        for (int i = 0; i < columns.length; i++) {
            if (i > 0) sql.append(",");
            sql.append(columns[i].name());
        }
        sql.append(" FROM ").append(tableName);
        boolean hasKeyword = keyword != null && !keyword.isBlank();
        if (hasKeyword) {
            sql.append(" WHERE ");
            for (int i = 0; i < columns.length; i++) {
                if (i > 0) sql.append(" OR ");
                sql.append("CAST(").append(columns[i].name()).append(" AS CHAR) LIKE ?");
            }
        }
        sql.append(" ORDER BY ").append(primaryKey);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            if (hasKeyword) {
                for (int i = 0; i < columns.length; i++) ps.setString(i + 1, "%" + keyword + "%");
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] row = new Object[columns.length];
                    for (int i = 0; i < columns.length; i++) {
                        Object value = rs.getObject(columns[i].name());
                        row[i] = value instanceof Date ? value.toString() : value;
                    }
                    model.addRow(row);
                }
            }
        } catch (SQLException ex) {
            showError(ex);
        }
    }

    private void clearInputs() {
        for (JTextField input : inputs) input.setText("");
        selectedKey = null;
        table.clearSelection();
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(this, "Có lỗi xảy ra:\n" + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}

package qlsv;

public record ColumnInfo(String name, String label, DataType type) {
    public enum DataType {
        STRING, INTEGER, DECIMAL, DATE
    }
}

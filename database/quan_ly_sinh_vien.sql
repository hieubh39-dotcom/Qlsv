CREATE DATABASE IF NOT EXISTS quan_ly_sinh_vien
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE quan_ly_sinh_vien;

CREATE TABLE IF NOT EXISTS giang_vien (
    ma_gv VARCHAR(20) PRIMARY KEY,
    ho_ten VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    so_dien_thoai VARCHAR(15),
    khoa VARCHAR(100)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sinh_vien (
    ma_sv VARCHAR(20) PRIMARY KEY,
    ho_ten VARCHAR(100) NOT NULL,
    ngay_sinh DATE,
    gioi_tinh VARCHAR(10),
    email VARCHAR(100),
    so_dien_thoai VARCHAR(15),
    dia_chi VARCHAR(255)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS mon_hoc (
    ma_mh VARCHAR(20) PRIMARY KEY,
    ten_mh VARCHAR(100) NOT NULL,
    so_tin_chi INT NOT NULL,
    ma_gv VARCHAR(20),
    CONSTRAINT fk_monhoc_giangvien FOREIGN KEY (ma_gv)
        REFERENCES giang_vien(ma_gv)
        ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS diem (
    ma_diem INT AUTO_INCREMENT PRIMARY KEY,
    ma_sv VARCHAR(20) NOT NULL,
    ma_mh VARCHAR(20) NOT NULL,
    diem_qua_trinh DECIMAL(4,2),
    diem_thi DECIMAL(4,2),
    CONSTRAINT fk_diem_sinhvien FOREIGN KEY (ma_sv)
        REFERENCES sinh_vien(ma_sv)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_diem_monhoc FOREIGN KEY (ma_mh)
        REFERENCES mon_hoc(ma_mh)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT uq_diem_sinhvien_monhoc UNIQUE (ma_sv, ma_mh),
    CONSTRAINT chk_diem_qt CHECK (diem_qua_trinh IS NULL OR diem_qua_trinh BETWEEN 0 AND 10),
    CONSTRAINT chk_diem_thi CHECK (diem_thi IS NULL OR diem_thi BETWEEN 0 AND 10)
) ENGINE=InnoDB;

-- Dữ liệu mẫu để kiểm tra chương trình
INSERT IGNORE INTO giang_vien VALUES
('GV01', 'Nguyễn Văn An', 'an@example.com', '0901000001', 'Công nghệ thông tin'),
('GV02', 'Trần Thị Bình', 'binh@example.com', '0901000002', 'Khoa học cơ bản');

INSERT IGNORE INTO sinh_vien VALUES
('SV01', 'Nguyễn Minh Anh', '2005-03-12', 'Nam', 'minhanh@example.com', '0912000001', 'Hà Nội'),
('SV02', 'Trần Thu Hà', '2005-08-21', 'Nữ', 'thuha@example.com', '0912000002', 'Bắc Ninh');

INSERT IGNORE INTO mon_hoc VALUES
('MH01', 'Lập trình Java', 3, 'GV01'),
('MH02', 'Cơ sở dữ liệu', 3, 'GV02');

INSERT IGNORE INTO diem (ma_sv, ma_mh, diem_qua_trinh, diem_thi) VALUES
('SV01', 'MH01', 8.5, 9.0),
('SV02', 'MH02', 7.5, 8.0);

package com.tannguyen.ai.model.secondary;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
    @Id
    @Column(name = "mssv", length = 50, nullable = false)
    private String mssv;

    @Column(name = "ho_ten", length = 100)
    private String hoTen;

    @Column(name = "gioi_tinh", length = 10)
    private String gioiTinh;

    @Column(name = "ngay_vao_truong", length = 50)
    private String ngayVaoTruong;

    @Column(name = "lop_hoc", length = 50)
    private String lopHoc;

    @Column(name = "co_so", length = 100)
    private String coSo;

    @Column(name = "bac_dao_tao", length = 50)
    private String bacDaoTao;

    @Column(name = "loai_hinh_dao_tao", length = 50)
    private String loaiHinhDaoTao;

    @Column(name = "khoa", length = 100)
    private String khoa;

    @Column(name = "nganh", length = 100)
    private String nganh;

    @Column(name = "chuyen_nganh", length = 100)
    private String chuyenNganh;

    @Column(name = "khoa_hoc", length = 50)
    private String khoaHoc;

    @Column(name = "noi_cap", length = 100)
    private String noiCap;

    @Column(name = "ngay_sinh", length = 50)
    private String ngaySinh;

    @Column(name = "so_cmnd", length = 50)
    private String soCmnd;

    @Column(name = "doi_tuong", length = 50)
    private String doiTuong;

    @Column(name = "ngay_vao_doan", length = 50)
    private String ngayVaoDoan;

    @Column(name = "dien_thoai", length = 50)
    private String dienThoai;

    @Column(name = "dia_chi_lien_he", length = 255)
    private String diaChiLienHe;

    @Column(name = "noi_sinh", length = 255)
    private String noiSinh;

    @Column(name = "ho_khau_thuong_tru", length = 255)
    private String hoKhauThuongTru;

    @Column(name = "email_dnc", length = 255)
    private String emailDnc;

    @Column(name = "mat_khau_email_dnc", length = 255)
    private String matKhauEmailDnc;

    @Column(name = "ma_ho_so", length = 50)
    private String maHoSo;
}
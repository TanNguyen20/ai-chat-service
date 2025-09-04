package com.tannguyen.ai.mapper;

import com.tannguyen.ai.dto.request.StudentRequestDTO;
import com.tannguyen.ai.dto.response.StudentResponseDTO;
import com.tannguyen.ai.model.secondary.Student;

public class StudentMapper {
    public static Student toEntity(StudentRequestDTO dto) {
        if (dto == null) return null;
        Student s = new Student();
        s.setMssv(dto.getMssv());
        s.setHoTen(dto.getHoTen());
        s.setGioiTinh(dto.getGioiTinh());
        s.setNgayVaoTruong(dto.getNgayVaoTruong());
        s.setLopHoc(dto.getLopHoc());
        s.setCoSo(dto.getCoSo());
        s.setBacDaoTao(dto.getBacDaoTao());
        s.setLoaiHinhDaoTao(dto.getLoaiHinhDaoTao());
        s.setKhoa(dto.getKhoa());
        s.setNganh(dto.getNganh());
        s.setChuyenNganh(dto.getChuyenNganh());
        s.setKhoaHoc(dto.getKhoaHoc());
        s.setNoiCap(dto.getNoiCap());
        s.setNgaySinh(dto.getNgaySinh());
        s.setSoCmnd(dto.getSoCmnd());
        s.setDoiTuong(dto.getDoiTuong());
        s.setNgayVaoDoan(dto.getNgayVaoDoan());
        s.setDienThoai(dto.getDienThoai());
        s.setDiaChiLienHe(dto.getDiaChiLienHe());
        s.setNoiSinh(dto.getNoiSinh());
        s.setHoKhauThuongTru(dto.getHoKhauThuongTru());
        s.setEmailDnc(dto.getEmailDnc());
        s.setMatKhauEmailDnc(dto.getMatKhauEmailDnc());
        s.setMaHoSo(dto.getMaHoSo());
        return s;
    }

    public static StudentResponseDTO toDto(Student s) {
        if (s == null) return null;
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setMssv(s.getMssv());
        dto.setHoTen(s.getHoTen());
        dto.setGioiTinh(s.getGioiTinh());
        dto.setNgayVaoTruong(s.getNgayVaoTruong());
        dto.setLopHoc(s.getLopHoc());
        dto.setCoSo(s.getCoSo());
        dto.setBacDaoTao(s.getBacDaoTao());
        dto.setLoaiHinhDaoTao(s.getLoaiHinhDaoTao());
        dto.setKhoa(s.getKhoa());
        dto.setNganh(s.getNganh());
        dto.setChuyenNganh(s.getChuyenNganh());
        dto.setKhoaHoc(s.getKhoaHoc());
        dto.setNoiCap(s.getNoiCap());
        dto.setNgaySinh(s.getNgaySinh());
        dto.setSoCmnd(s.getSoCmnd());
        dto.setDoiTuong(s.getDoiTuong());
        dto.setNgayVaoDoan(s.getNgayVaoDoan());
        dto.setDienThoai(s.getDienThoai());
        dto.setDiaChiLienHe(s.getDiaChiLienHe());
        dto.setNoiSinh(s.getNoiSinh());
        dto.setHoKhauThuongTru(s.getHoKhauThuongTru());
        dto.setEmailDnc(s.getEmailDnc());
        dto.setMatKhauEmailDnc(s.getMatKhauEmailDnc());
        dto.setMaHoSo(s.getMaHoSo());
        return dto;
    }

    public static void merge(StudentRequestDTO src, Student dst) {
        if (src.getHoTen() != null) dst.setHoTen(src.getHoTen());
        if (src.getGioiTinh() != null) dst.setGioiTinh(src.getGioiTinh());
        if (src.getNgayVaoTruong() != null) dst.setNgayVaoTruong(src.getNgayVaoTruong());
        if (src.getLopHoc() != null) dst.setLopHoc(src.getLopHoc());
        if (src.getCoSo() != null) dst.setCoSo(src.getCoSo());
        if (src.getBacDaoTao() != null) dst.setBacDaoTao(src.getBacDaoTao());
        if (src.getLoaiHinhDaoTao() != null) dst.setLoaiHinhDaoTao(src.getLoaiHinhDaoTao());
        if (src.getKhoa() != null) dst.setKhoa(src.getKhoa());
        if (src.getNganh() != null) dst.setNganh(src.getNganh());
        if (src.getChuyenNganh() != null) dst.setChuyenNganh(src.getChuyenNganh());
        if (src.getKhoaHoc() != null) dst.setKhoaHoc(src.getKhoaHoc());
        if (src.getNoiCap() != null) dst.setNoiCap(src.getNoiCap());
        if (src.getNgaySinh() != null) dst.setNgaySinh(src.getNgaySinh());
        if (src.getSoCmnd() != null) dst.setSoCmnd(src.getSoCmnd());
        if (src.getDoiTuong() != null) dst.setDoiTuong(src.getDoiTuong());
        if (src.getNgayVaoDoan() != null) dst.setNgayVaoDoan(src.getNgayVaoDoan());
        if (src.getDienThoai() != null) dst.setDienThoai(src.getDienThoai());
        if (src.getDiaChiLienHe() != null) dst.setDiaChiLienHe(src.getDiaChiLienHe());
        if (src.getNoiSinh() != null) dst.setNoiSinh(src.getNoiSinh());
        if (src.getHoKhauThuongTru() != null) dst.setHoKhauThuongTru(src.getHoKhauThuongTru());
        if (src.getEmailDnc() != null) dst.setEmailDnc(src.getEmailDnc());
        if (src.getMatKhauEmailDnc() != null) dst.setMatKhauEmailDnc(src.getMatKhauEmailDnc());
        if (src.getMaHoSo() != null) dst.setMaHoSo(src.getMaHoSo());
    }
}

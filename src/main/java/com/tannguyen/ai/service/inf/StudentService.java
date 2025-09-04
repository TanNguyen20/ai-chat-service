package com.tannguyen.ai.service.inf;

import com.tannguyen.ai.dto.request.StudentRequestDTO;
import com.tannguyen.ai.dto.response.StudentResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface StudentService {
    Page<StudentResponseDTO> list(
            List<String> gioiTinh,
            List<String> coSo,
            List<String> bacDaoTao,
            List<String> loaiHinhDaoTao,
            List<String> khoa,
            List<String> nganh,
            Pageable pageable
    );

    Page<StudentResponseDTO> searchByName(
            String name,
            List<String> gioiTinh,
            List<String> coSo,
            List<String> bacDaoTao,
            List<String> loaiHinhDaoTao,
            List<String> khoa,
            List<String> nganh,
            Pageable pageable
    );

    StudentResponseDTO getById(String mssv);

    void create(StudentRequestDTO request);

    void update(String mssv, StudentRequestDTO request);

    void delete(String mssv);

    Map<String, List<String>> facets();
}
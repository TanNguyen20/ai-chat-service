package com.tannguyen.ai.service.inf;

import com.tannguyen.ai.dto.StudentColumnDTO;
import com.tannguyen.ai.dto.request.StudentRequestDTO;
import com.tannguyen.ai.dto.response.StudentResponseDTO;
import com.tannguyen.ai.enums.StudentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface StudentService {

    Page<StudentResponseDTO> list(
            StudentType studentType,
            List<String> gioiTinh,
            List<String> coSo,
            List<String> bacDaoTao,
            List<String> loaiHinhDaoTao,
            List<String> khoa,
            List<String> nganh,
            Pageable pageable
    );

    Page<StudentResponseDTO> search(
            StudentType studentType,
            String query,
            List<String> gioiTinh,
            List<String> coSo,
            List<String> bacDaoTao,
            List<String> loaiHinhDaoTao,
            List<String> khoa,
            List<String> nganh,
            Pageable pageable
    );

    Page<StudentResponseDTO> searchByName(
            StudentType studentType,
            String name,
            List<String> gioiTinh,
            List<String> coSo,
            List<String> bacDaoTao,
            List<String> loaiHinhDaoTao,
            List<String> khoa,
            List<String> nganh,
            Pageable pageable
    );

    StudentResponseDTO getById(StudentType studentType, String mssv);

    void create(StudentType studentType, StudentRequestDTO request);

    void update(StudentType studentType, String mssv, StudentRequestDTO request);

    void delete(StudentType studentType, String mssv);

    Map<String, List<String>> facets(StudentType studentType);

    List<StudentColumnDTO> getColumns();
}

package com.tannguyen.ai.service.impl;

import com.tannguyen.ai.dto.request.StudentRequestDTO;
import com.tannguyen.ai.dto.response.StudentResponseDTO;
import com.tannguyen.ai.mapper.StudentMapper;
import com.tannguyen.ai.model.secondary.Student;
import com.tannguyen.ai.repository.secondary.StudentRepository;
import com.tannguyen.ai.service.inf.StudentService;
import com.tannguyen.ai.spec.StudentSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repository;

    @Override
    public Page<StudentResponseDTO> list(List<String> gioiTinh, List<String> coSo, List<String> bacDaoTao,
                                         List<String> loaiHinhDaoTao, List<String> khoa, List<String> nganh,
                                         Pageable pageable) {
        Specification<Student> spec = StudentSpecifications.filter(gioiTinh, coSo, bacDaoTao, loaiHinhDaoTao, khoa, nganh);
        Page<Student> page = repository.findAll(spec, pageable);
        return page.map(StudentMapper::toDto);
    }

    @Override
    public Page<StudentResponseDTO> searchByName(String name, List<String> gioiTinh, List<String> coSo, List<String> bacDaoTao,
                                                 List<String> loaiHinhDaoTao, List<String> khoa, List<String> nganh,
                                                 Pageable pageable) {
        Specification<Student> spec =
                Specification.where(StudentSpecifications.nameContainsIgnoreCase(name))
                        .and(StudentSpecifications.filter(gioiTinh, coSo, bacDaoTao, loaiHinhDaoTao, khoa, nganh));
        Page<Student> page = repository.findAll(spec, pageable);
        return page.map(StudentMapper::toDto);
    }

    @Override
    public StudentResponseDTO getById(String mssv) {
        Student s = repository.findById(mssv).orElseThrow(() -> new IllegalArgumentException("Student not found: " + mssv));
        return StudentMapper.toDto(s);
    }

    @Override
    public void create(StudentRequestDTO request) {
        if (request.getMssv() == null || request.getMssv().isBlank())
            throw new IllegalArgumentException("mssv is required");
        if (repository.existsById(request.getMssv()))
            throw new IllegalArgumentException("mssv already exists");
        repository.save(StudentMapper.toEntity(request));
    }

    @Override
    public void update(String mssv, StudentRequestDTO request) {
        Student s = repository.findById(mssv).orElseThrow(() -> new IllegalArgumentException("Student not found: " + mssv));
        StudentMapper.merge(request, s);
        // keep primary key stable
        s.setMssv(mssv);
        repository.save(s);
    }

    @Override
    public void delete(String mssv) {
        if (!repository.existsById(mssv)) return;
        repository.deleteById(mssv);
    }

    @Override
    public Map<String, List<String>> facets() {
        return Map.of(
                "coSo", repository.distinctCoSo(),
                "khoa", repository.distinctKhoa(),
                "nganh", repository.distinctNganh(),
                "gioiTinh", repository.distinctGioiTinh(),
                "bacDaoTao", repository.distinctBacDaoTao(),
                "loaiHinhDaoTao", repository.distinctLoaiHinhDaoTao()
        );
    }
}
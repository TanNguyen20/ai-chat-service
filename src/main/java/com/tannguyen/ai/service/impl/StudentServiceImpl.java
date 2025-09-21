package com.tannguyen.ai.service.impl;

import com.tannguyen.ai.dto.request.StudentRequestDTO;
import com.tannguyen.ai.dto.response.StudentResponseDTO;
import com.tannguyen.ai.enums.StudentType;
import com.tannguyen.ai.mapper.StudentMapper;
import com.tannguyen.ai.model.secondary.StudentBase;
import com.tannguyen.ai.model.secondary.StudentDNC;
import com.tannguyen.ai.model.secondary.StudentUSH;
import com.tannguyen.ai.repository.secondary.StudentDNCRepository;
import com.tannguyen.ai.repository.secondary.StudentFacetRepository;
import com.tannguyen.ai.repository.secondary.StudentUSHRepository;
import com.tannguyen.ai.service.inf.StudentService;
import com.tannguyen.ai.spec.StudentSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentDNCRepository dncRepo;
    private final StudentUSHRepository ushRepo;

    @Override
    public Page<StudentResponseDTO> list(
            StudentType studentType,
            List<String> gioiTinh, List<String> coSo, List<String> bacDaoTao,
            List<String> loaiHinhDaoTao, List<String> khoa, List<String> nganh,
            Pageable pageable
    ) {
        return switch (studentType) {
            case DNC -> {
                Specification<StudentDNC> spec = StudentSpecifications.<StudentDNC>filter(
                        gioiTinh, coSo, bacDaoTao, loaiHinhDaoTao, khoa, nganh
                );
                yield dncRepo.findAll(spec, pageable).map(StudentMapper::toDto);
            }
            case USH -> {
                Specification<StudentUSH> spec = StudentSpecifications.<StudentUSH>filter(
                        gioiTinh, coSo, bacDaoTao, loaiHinhDaoTao, khoa, nganh
                );
                yield ushRepo.findAll(spec, pageable).map(StudentMapper::toDto);
            }
            case ALL -> {
                // You can implement a true cross-table page if needed; here we page per-table then merge.
                // Simplest: choose one for now or throw UnsupportedOperationException.
                throw new UnsupportedOperationException("Paging across both tables not implemented.");
            }
        };
    }

    @Override
    public Page<StudentResponseDTO> search(
            StudentType studentType,
            String query,
            List<String> gioiTinh, List<String> coSo, List<String> bacDaoTao,
            List<String> loaiHinhDaoTao, List<String> khoa, List<String> nganh,
            Pageable pageable
    ) {
        return switch (studentType) {
            case DNC -> {
                Specification<StudentDNC> spec = Specification
                        .where(StudentSpecifications.<StudentDNC>freeText(query))
                        .and(StudentSpecifications.<StudentDNC>filter(gioiTinh, coSo, bacDaoTao, loaiHinhDaoTao, khoa, nganh));
                yield dncRepo.findAll(spec, pageable).map(StudentMapper::toDto);
            }
            case USH -> {
                Specification<StudentUSH> spec = Specification
                        .where(StudentSpecifications.<StudentUSH>freeText(query))
                        .and(StudentSpecifications.<StudentUSH>filter(gioiTinh, coSo, bacDaoTao, loaiHinhDaoTao, khoa, nganh));
                yield ushRepo.findAll(spec, pageable).map(StudentMapper::toDto);
            }
            case ALL -> throw new UnsupportedOperationException("Cross-table search paging not implemented.");
        };
    }

    @Override
    public Page<StudentResponseDTO> searchByName(
            StudentType studentType,
            String name,
            List<String> gioiTinh, List<String> coSo, List<String> bacDaoTao,
            List<String> loaiHinhDaoTao, List<String> khoa, List<String> nganh,
            Pageable pageable
    ) {
        return switch (studentType) {
            case DNC -> {
                Specification<StudentDNC> spec = Specification
                        .where(StudentSpecifications.<StudentDNC>nameContainsIgnoreCase(name))
                        .and(StudentSpecifications.<StudentDNC>filter(gioiTinh, coSo, bacDaoTao, loaiHinhDaoTao, khoa, nganh));
                yield dncRepo.findAll(spec, pageable).map(StudentMapper::toDto);
            }
            case USH -> {
                Specification<StudentUSH> spec = Specification
                        .where(StudentSpecifications.<StudentUSH>nameContainsIgnoreCase(name))
                        .and(StudentSpecifications.<StudentUSH>filter(gioiTinh, coSo, bacDaoTao, loaiHinhDaoTao, khoa, nganh));
                yield ushRepo.findAll(spec, pageable).map(StudentMapper::toDto);
            }
            case ALL -> throw new UnsupportedOperationException("Cross-table search paging not implemented.");
        };
    }

    @Override
    public StudentResponseDTO getById(StudentType studentType, String mssv) {
        return switch (studentType) {
            case DNC -> dncRepo.findById(mssv).map(StudentMapper::toDto)
                    .orElseThrow(() -> new IllegalArgumentException("Student DNC not found: " + mssv));
            case USH -> ushRepo.findById(mssv).map(StudentMapper::toDto)
                    .orElseThrow(() -> new IllegalArgumentException("Student USH not found: " + mssv));
            case ALL -> throw new IllegalArgumentException("School name must be DNC or USH for getById");
        };
    }

    @Override
    public void create(StudentType studentType, StudentRequestDTO request) {
        if (request.getMssv() == null || request.getMssv().isBlank())
            throw new IllegalArgumentException("mssv is required");

        switch (studentType) {
            case DNC -> {
                if (dncRepo.existsById(request.getMssv()))
                    throw new IllegalArgumentException("mssv already exists (DNC)");
                dncRepo.save(StudentMapper.toEntityDNC(request));
            }
            case USH -> {
                if (ushRepo.existsById(request.getMssv()))
                    throw new IllegalArgumentException("mssv already exists (USH)");
                ushRepo.save(StudentMapper.toEntityUSH(request));
            }
            case ALL -> throw new IllegalArgumentException("School name must be DNC or USH for create");
        }
    }

    @Override
    public void update(StudentType studentType, String mssv, StudentRequestDTO request) {
        switch (studentType) {
            case DNC -> {
                StudentDNC s = dncRepo.findById(mssv)
                        .orElseThrow(() -> new IllegalArgumentException("Student DNC not found: " + mssv));
                StudentMapper.merge(request, s);
                s.setMssv(mssv);
                dncRepo.save(s);
            }
            case USH -> {
                StudentUSH s = ushRepo.findById(mssv)
                        .orElseThrow(() -> new IllegalArgumentException("Student USH not found: " + mssv));
                StudentMapper.merge(request, s);
                s.setMssv(mssv);
                ushRepo.save(s);
            }
            case ALL -> throw new IllegalArgumentException("School name must be DNC or USH for update");
        }
    }

    @Override
    public void delete(StudentType studentType, String mssv) {
        switch (studentType) {
            case DNC -> { if (dncRepo.existsById(mssv)) dncRepo.deleteById(mssv); }
            case USH -> { if (ushRepo.existsById(mssv)) ushRepo.deleteById(mssv); }
            case ALL -> throw new IllegalArgumentException("School name must be DNC or USH for delete");
        }
    }

    @Override
    public Map<String, List<String>> facets(StudentType studentType) {
        return switch (studentType) {
            case DNC -> toFacetMap(dncRepo);
            case USH -> toFacetMap(ushRepo);
            case ALL -> mergeFacetMaps(toFacetMap(dncRepo), toFacetMap(ushRepo));
        };
    }

    private static Map<String, List<String>> toFacetMap(StudentFacetRepository r) {
        return Map.of(
                "coSo", r.distinctCoSo(),
                "khoa", r.distinctKhoa(),
                "nganh", r.distinctNganh(),
                "gioiTinh", r.distinctGioiTinh(),
                "bacDaoTao", r.distinctBacDaoTao(),
                "loaiHinhDaoTao", r.distinctLoaiHinhDaoTao()
        );
    }

    private static Map<String, List<String>> mergeFacetMaps(Map<String, List<String>> a, Map<String, List<String>> b) {
        Map<String, List<String>> out = new HashMap<>();
        for (String k : a.keySet()) {
            Set<String> set = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
            set.addAll(a.getOrDefault(k, List.of()));
            set.addAll(b.getOrDefault(k, List.of()));
            out.put(k, List.copyOf(set));
        }
        return out;
    }
}

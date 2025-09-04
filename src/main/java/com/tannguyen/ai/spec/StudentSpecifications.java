package com.tannguyen.ai.spec;

import com.tannguyen.ai.model.secondary.Student;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class StudentSpecifications {

    public static Specification<Student> nameContainsIgnoreCase(String name) {
        if (name == null || name.isBlank()) return null;
        final String like = "%" + name.toLowerCase() + "%";
        return (root, q, cb) -> cb.like(cb.lower(root.get("hoTen")), like);
    }

    private static Specification<Student> inIfPresent(String field, List<String> values) {
        if (values == null || values.isEmpty()) return null;
        return (root, q, cb) -> root.get(field).in(values);
    }

    public static Specification<Student> filter(
            List<String> gioiTinh,
            List<String> coSo,
            List<String> bacDaoTao,
            List<String> loaiHinhDaoTao,
            List<String> khoa,
            List<String> nganh
    ) {
        return Specification
                .where(inIfPresent("gioiTinh", gioiTinh))
                .and(inIfPresent("coSo", coSo))
                .and(inIfPresent("bacDaoTao", bacDaoTao))
                .and(inIfPresent("loaiHinhDaoTao", loaiHinhDaoTao))
                .and(inIfPresent("khoa", khoa))
                .and(inIfPresent("nganh", nganh));
    }
}
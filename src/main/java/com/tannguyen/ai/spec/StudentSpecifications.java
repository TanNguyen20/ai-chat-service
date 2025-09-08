package com.tannguyen.ai.spec;

import com.tannguyen.ai.model.secondary.Student;
import jakarta.persistence.criteria.Predicate;
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

    public static Specification<Student> freeText(String query) {
        if (query == null || query.isBlank()) return null;
        final String[] tokens = query.trim().toLowerCase().split("\\s+");
        return (root, q, cb) -> {
            // target fields: hoTen, diaChiLienHe, hoKhauThuongTru, noiSinh
            var hoTen   = cb.lower(root.get("hoTen"));
            var lienHe  = cb.lower(root.get("diaChiLienHe"));
            var hoKhau  = cb.lower(root.get("hoKhauThuongTru"));
            var noiSinh = cb.lower(root.get("noiSinh"));

            Predicate andAllTokens = cb.conjunction();
            for (String t : tokens) {
                String like = "%" + t + "%";
                var tokenMatchesAnyField = cb.or(
                        cb.like(hoTen, like),
                        cb.like(lienHe, like),
                        cb.like(hoKhau, like),
                        cb.like(noiSinh, like)
                );
                andAllTokens = cb.and(andAllTokens, tokenMatchesAnyField);
            }
            return andAllTokens;
        };
    }
}

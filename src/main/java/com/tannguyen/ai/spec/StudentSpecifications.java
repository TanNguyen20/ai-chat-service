package com.tannguyen.ai.spec;

import com.tannguyen.ai.model.secondary.StudentBase;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class StudentSpecifications {

    public static <T extends StudentBase> Specification<T> nameContainsIgnoreCase(String name) {
        if (name == null || name.isBlank()) return null;
        final String like = "%" + name.toLowerCase() + "%";
        return (root, q, cb) -> cb.like(cb.lower(root.get("hoTen")), like);
    }

    private static <T extends StudentBase> Specification<T> inIfPresent(String field, List<String> values) {
        if (values == null || values.isEmpty()) return null;
        return (root, q, cb) -> root.get(field).in(values);
    }

    public static <T extends StudentBase> Specification<T> filter(
            List<String> gioiTinh,
            List<String> coSo,
            List<String> bacDaoTao,
            List<String> loaiHinhDaoTao,
            List<String> khoa,
            List<String> nganh
    ) {
        return Specification.<T>where(StudentSpecifications.<T>inIfPresent("gioiTinh", gioiTinh))
                .and(StudentSpecifications.<T>inIfPresent("coSo", coSo))
                .and(StudentSpecifications.<T>inIfPresent("bacDaoTao", bacDaoTao))
                .and(StudentSpecifications.<T>inIfPresent("loaiHinhDaoTao", loaiHinhDaoTao))
                .and(StudentSpecifications.<T>inIfPresent("khoa", khoa))
                .and(StudentSpecifications.<T>inIfPresent("nganh", nganh));
    }

    public static <T extends StudentBase> Specification<T> freeText(String query) {
        if (query == null || query.isBlank()) return null;
        final String[] tokens = query.trim().toLowerCase().split("\\s+");

        return (root, q, cb) -> {
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

package com.tannguyen.ai.controller;

import com.tannguyen.ai.dto.request.StudentRequestDTO;
import com.tannguyen.ai.dto.response.ResponseFactory;
import com.tannguyen.ai.enums.StudentType;
import com.tannguyen.ai.service.inf.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.tannguyen.ai.constant.CommonConstant.API_V1;

@RestController
@RequestMapping(API_V1 + "/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<?> list(
            @RequestParam(defaultValue = "DNC") StudentType studentType,
            @RequestParam(required = false) List<String> gioiTinh,
            @RequestParam(required = false) List<String> coSo,
            @RequestParam(required = false) List<String> bacDaoTao,
            @RequestParam(required = false) List<String> loaiHinhDaoTao,
            @RequestParam(required = false) List<String> khoa,
            @RequestParam(required = false) List<String> nganh,
            @PageableDefault Pageable pageable
    ) {
        var page = studentService.list(studentType, gioiTinh, coSo, bacDaoTao, loaiHinhDaoTao, khoa, nganh, pageable);
        return ResponseFactory.success(page, "Get students successfully", HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(defaultValue = "DNC") StudentType studentType,
            @RequestParam(name = "q", required = false) String q,
            @RequestParam(name = "name", required = false) String legacyName,
            @RequestParam(required = false) List<String> gioiTinh,
            @RequestParam(required = false) List<String> coSo,
            @RequestParam(required = false) List<String> bacDaoTao,
            @RequestParam(required = false) List<String> loaiHinhDaoTao,
            @RequestParam(required = false) List<String> khoa,
            @RequestParam(required = false) List<String> nganh,
            @PageableDefault Pageable pageable
    ) {
        String query = (q != null && !q.isBlank()) ? q : legacyName;
        var page = studentService.search(studentType, query, gioiTinh, coSo, bacDaoTao, loaiHinhDaoTao, khoa, nganh, pageable);
        return ResponseFactory.success(page, "Search students successfully", HttpStatus.OK);
    }

    // Search by name + filters
    @GetMapping("/search-by-name")
    public ResponseEntity<?> searchByName(
            @RequestParam(defaultValue = "DNC") StudentType studentType,
            @RequestParam String name,
            @RequestParam(required = false) List<String> gioiTinh,
            @RequestParam(required = false) List<String> coSo,
            @RequestParam(required = false) List<String> bacDaoTao,
            @RequestParam(required = false) List<String> loaiHinhDaoTao,
            @RequestParam(required = false) List<String> khoa,
            @RequestParam(required = false) List<String> nganh,
            @PageableDefault Pageable pageable
    ) {
        var page = studentService.searchByName(studentType, name, gioiTinh, coSo, bacDaoTao, loaiHinhDaoTao, khoa, nganh, pageable);
        return ResponseFactory.success(page, "Search students successfully", HttpStatus.OK);
    }

    @GetMapping("/{mssv}")
    public ResponseEntity<?> get(
            @RequestParam(defaultValue = "DNC") StudentType studentType,
            @PathVariable String mssv
    ) {
        return ResponseFactory.success(studentService.getById(studentType, mssv), "Get student successfully", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestParam(defaultValue = "DNC") StudentType studentType,
            @RequestBody StudentRequestDTO request
    ) {
        studentService.create(studentType, request);
        return ResponseFactory.success(null, "Create student successfully", HttpStatus.CREATED);
    }

    @PutMapping("/{mssv}")
    public ResponseEntity<?> update(
            @RequestParam(defaultValue = "DNC") StudentType studentType,
            @PathVariable String mssv,
            @RequestBody StudentRequestDTO request
    ) {
        studentService.update(studentType, mssv, request);
        return ResponseFactory.success(null, "Update student successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{mssv}")
    public ResponseEntity<?> delete(
            @RequestParam(defaultValue = "DNC") StudentType studentType,
            @PathVariable String mssv
    ) {
        studentService.delete(studentType, mssv);
        return ResponseFactory.success(null, "Delete student successfully", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/facets")
    public ResponseEntity<?> facets(
            @RequestParam(defaultValue = "DNC") StudentType studentType
    ) {
        return ResponseFactory.success(studentService.facets(studentType), "Get facets successfully", HttpStatus.OK);
    }
}

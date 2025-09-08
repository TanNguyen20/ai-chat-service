package com.tannguyen.ai.controller;

import com.tannguyen.ai.dto.request.StudentRequestDTO;
import com.tannguyen.ai.dto.response.ResponseFactory;
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

    // List with optional filters
    @GetMapping
    public ResponseEntity<?> list(
            @RequestParam(required = false) List<String> gioiTinh,
            @RequestParam(required = false) List<String> coSo,
            @RequestParam(required = false) List<String> bacDaoTao,
            @RequestParam(required = false) List<String> loaiHinhDaoTao,
            @RequestParam(required = false) List<String> khoa,
            @RequestParam(required = false) List<String> nganh,
            @PageableDefault Pageable pageable
    ) {
        var page = studentService.list(gioiTinh, coSo, bacDaoTao, loaiHinhDaoTao, khoa, nganh, pageable);
        return ResponseFactory.success(page, "Get students successfully", HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(
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
        var page = studentService.search(query, gioiTinh, coSo, bacDaoTao, loaiHinhDaoTao, khoa, nganh, pageable);
        return ResponseFactory.success(page, "Search students successfully", HttpStatus.OK);
    }

    // Search by name + filters
    @GetMapping("/search-by-name")
    public ResponseEntity<?> searchByName(
            @RequestParam String name,
            @RequestParam(required = false) List<String> gioiTinh,
            @RequestParam(required = false) List<String> coSo,
            @RequestParam(required = false) List<String> bacDaoTao,
            @RequestParam(required = false) List<String> loaiHinhDaoTao,
            @RequestParam(required = false) List<String> khoa,
            @RequestParam(required = false) List<String> nganh,
            @PageableDefault Pageable pageable
    ) {
        var page = studentService.searchByName(name, gioiTinh, coSo, bacDaoTao, loaiHinhDaoTao, khoa, nganh, pageable);
        return ResponseFactory.success(page, "Search students successfully", HttpStatus.OK);
    }

    @GetMapping("/{mssv}")
    public ResponseEntity<?> get(@PathVariable String mssv) {
        return ResponseFactory.success(studentService.getById(mssv), "Get student successfully", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody StudentRequestDTO request) {
        studentService.create(request);
        return ResponseFactory.success(null, "Create student successfully", HttpStatus.CREATED);
    }

    @PutMapping("/{mssv}")
    public ResponseEntity<?> update(@PathVariable String mssv, @RequestBody StudentRequestDTO request) {
        studentService.update(mssv, request);
        return ResponseFactory.success(null, "Update student successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{mssv}")
    public ResponseEntity<?> delete(@PathVariable String mssv) {
        studentService.delete(mssv);
        return ResponseFactory.success(null, "Delete student successfully", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/facets")
    public ResponseEntity<?> facets() {
        return ResponseFactory.success(studentService.facets(), "Get facets successfully", HttpStatus.OK);
    }
}

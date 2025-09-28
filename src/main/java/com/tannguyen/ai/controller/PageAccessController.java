package com.tannguyen.ai.controller;

import com.tannguyen.ai.dto.request.UpsertPageRequestDTO;
import com.tannguyen.ai.dto.response.ResponseFactory;
import com.tannguyen.ai.service.inf.PageAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.tannguyen.ai.constant.CommonConstant.API_V1;

@RestController
@RequestMapping(API_V1 + "/pages")
@RequiredArgsConstructor
public class PageAccessController {

    private final PageAccessService pageAccessService;

    @GetMapping("")
    public ResponseEntity<?> listAllPages() {
        return ResponseFactory.success(
                pageAccessService.list(),
                "Get page access list successfully",
                HttpStatus.OK
        );
    }

    @GetMapping("/pagination")
    public ResponseEntity<?> listPagesPagination(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("url").ascending());
        return ResponseFactory.success(
                pageAccessService.listPagination(pageable),
                "Get page access list successfully",
                HttpStatus.OK
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getPage(@PathVariable Long id) {
        return ResponseFactory.success(
                pageAccessService.get(id),
                "Get page access successfully",
                HttpStatus.OK
        );
    }

    @PostMapping("")
    public ResponseEntity<?> createPage(@RequestBody UpsertPageRequestDTO request) {
        return ResponseFactory.success(
                pageAccessService.create(request),
                "Create page access successfully",
                HttpStatus.CREATED
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updatePage(@PathVariable Long id, @RequestBody UpsertPageRequestDTO request) {
        return ResponseFactory.success(
                pageAccessService.update(id, request),
                "Update page access successfully",
                HttpStatus.OK
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletePage(@PathVariable Long id) {
        pageAccessService.delete(id);
        return ResponseFactory.success(
                null,
                "Delete page access successfully",
                HttpStatus.NO_CONTENT
        );
    }
}
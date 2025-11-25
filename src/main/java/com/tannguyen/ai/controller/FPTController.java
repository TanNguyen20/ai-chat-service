package com.tannguyen.ai.controller;

import com.tannguyen.ai.dto.response.ResponseFactory;
import com.tannguyen.ai.enums.FPTType;
import com.tannguyen.ai.service.inf.FPTService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.tannguyen.ai.constant.CommonConstant.API_V1;

@RestController
@RequestMapping(API_V1 + "/fpt")
@RequiredArgsConstructor
public class FPTController {
    private final FPTService fptService;

    @GetMapping
    public ResponseEntity<?> list(
            @RequestParam(defaultValue = "FPT") FPTType fptType,
            @RequestParam(required = false) List<String> odataType,
            @RequestParam(required = false) List<String> displayName,
            @RequestParam(required = false) List<String> givenName,
            @RequestParam(required = false) List<String> surname,
            @RequestParam(required = false) List<String> jobTitle,
            @RequestParam(required = false) List<String> mail,
            @RequestParam(required = false) List<String> mobilePhone,
            @RequestParam(required = false) List<String> userPrincipalName,
            @RequestParam(required = false) List<String> officeLocation,
            @RequestParam(required = false) List<String> preferredLanguage,
            @PageableDefault Pageable pageable
    ) {
        var page = fptService.list(
                fptType, odataType, displayName, givenName, surname, jobTitle,
                mail, mobilePhone, userPrincipalName, officeLocation,
                preferredLanguage, pageable
        );
        return ResponseFactory.success(page, "Get FPT Extend records successfully", HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(defaultValue = "FPT") FPTType fptType,
            @RequestParam(name = "q", required = false) String q,
            @RequestParam(required = false) List<String> odataType,
            @RequestParam(required = false) List<String> displayName,
            @RequestParam(required = false) List<String> givenName,
            @RequestParam(required = false) List<String> surname,
            @RequestParam(required = false) List<String> jobTitle,
            @RequestParam(required = false) List<String> mail,
            @RequestParam(required = false) List<String> mobilePhone,
            @RequestParam(required = false) List<String> userPrincipalName,
            @RequestParam(required = false) List<String> officeLocation,
            @RequestParam(required = false) List<String> preferredLanguage,
            @PageableDefault Pageable pageable
    ) {
        var page = fptService.search(
                fptType, q, odataType, displayName, givenName, surname, jobTitle,
                mail, mobilePhone, userPrincipalName, officeLocation,
                preferredLanguage, pageable
        );
        return ResponseFactory.success(page, "Search FPT records successfully", HttpStatus.OK);
    }

    @GetMapping("/search-by-name")
    public ResponseEntity<?> searchByName(
            @RequestParam(defaultValue = "FPT") FPTType fptType,
            @RequestParam String name,
            @RequestParam(required = false) List<String> odataType,
            @RequestParam(required = false) List<String> displayName,
            @RequestParam(required = false) List<String> givenName,
            @RequestParam(required = false) List<String> surname,
            @RequestParam(required = false) List<String> jobTitle,
            @RequestParam(required = false) List<String> mail,
            @RequestParam(required = false) List<String> mobilePhone,
            @RequestParam(required = false) List<String> userPrincipalName,
            @RequestParam(required = false) List<String> officeLocation,
            @RequestParam(required = false) List<String> preferredLanguage,
            @PageableDefault Pageable pageable
    ) {
        var page = fptService.searchByName(
                fptType, name, odataType, displayName, givenName, surname, jobTitle,
                mail, mobilePhone, userPrincipalName, officeLocation,
                preferredLanguage, pageable
        );
        return ResponseFactory.success(page, "Search FPT Extend records successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@RequestParam(defaultValue = "FPT") FPTType fptType, @PathVariable String id) {
        var data = fptService.getById(fptType, id);
        return ResponseFactory.success(data, "Get FPT Extend record successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@RequestParam(defaultValue = "FPT") FPTType fptType, @PathVariable String id) {
        fptService.deleteById(fptType, id);
        return ResponseFactory.success(null, "Delete FPT Extend record successfully", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/facets")
    public ResponseEntity<?> facets(@RequestParam(defaultValue = "FPT") FPTType fptType) {
        var facets = fptService.facets(fptType);
        return ResponseFactory.success(facets, "Get FPT Extend facets successfully", HttpStatus.OK);
    }
}

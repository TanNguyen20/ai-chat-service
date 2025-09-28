package com.tannguyen.ai.service.inf;

import com.tannguyen.ai.dto.PageAccessDTO;
import com.tannguyen.ai.dto.request.UpsertPageRequestDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PageAccessService {

    /**
     * List pages with their per-role CRUD permissions.
     */
    List<PageAccessDTO> list();

    /**
     * List pages use pagination with their per-role CRUD permissions.
     */
    Page<PageAccessDTO> listPagination(Pageable pageable);

    /**
     * Get a single page by id.
     */
    PageAccessDTO get(Long id);

    /**
     * Create a new page with role permissions.
     */
    PageAccessDTO create(@Valid UpsertPageRequestDTO req);

    /**
     * Update an existing page and its role permissions.
     */
    PageAccessDTO update(Long id, @Valid UpsertPageRequestDTO req);

    /**
     * Delete a page and its associated role-permission mappings.
     */
    void delete(Long id);
}
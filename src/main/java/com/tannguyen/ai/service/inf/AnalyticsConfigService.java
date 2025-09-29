package com.tannguyen.ai.service.inf;

import com.tannguyen.ai.dto.request.AnalyticsConfigRequestDTO;
import com.tannguyen.ai.dto.response.AnalyticsConfigResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AnalyticsConfigService {
    void create(AnalyticsConfigRequestDTO requestDTO);

    AnalyticsConfigResponseDTO getById(Long id);

    Page<AnalyticsConfigResponseDTO> getAll(Pageable pageable);

    void update(Long id, AnalyticsConfigRequestDTO requestDTO);

    void delete(Long id);
}

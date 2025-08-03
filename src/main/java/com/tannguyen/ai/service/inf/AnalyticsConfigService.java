package com.tannguyen.ai.service.inf;

import com.tannguyen.ai.dto.request.AnalyticsConfigRequestDTO;
import com.tannguyen.ai.dto.response.AnalyticsConfigResponseDTO;

import java.util.List;

public interface AnalyticsConfigService {
    void create(AnalyticsConfigRequestDTO requestDTO);

    AnalyticsConfigResponseDTO getById(Long id);

    List<AnalyticsConfigResponseDTO> getAll();

    void update(Long id, AnalyticsConfigRequestDTO requestDTO);

    void delete(Long id);
}

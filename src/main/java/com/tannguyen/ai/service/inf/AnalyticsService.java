package com.tannguyen.ai.service.inf;

import com.tannguyen.ai.dto.request.AnalyticsRequestDTO;
import com.tannguyen.ai.dto.response.AnalyticsResponseDTO;

import java.util.List;

public interface AnalyticsService {
    List<AnalyticsResponseDTO> getAnalyticsList();

    AnalyticsResponseDTO getAnalyticsById(Long id);

    void addAnalytics(AnalyticsRequestDTO request);

    void updateAnalytics(Long id, AnalyticsRequestDTO request);

    void deleteAnalytics(Long id);
}

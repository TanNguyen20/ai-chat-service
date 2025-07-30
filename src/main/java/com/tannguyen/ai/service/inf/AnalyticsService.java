package com.tannguyen.ai.service.inf;

import com.tannguyen.ai.dto.request.AnalyticsRequestDTO;
import com.tannguyen.ai.model.Analytics;

import java.util.List;

public interface AnalyticsService {
    List<Analytics> getAnalyticsList();

    void addAnalytics(AnalyticsRequestDTO request);

    void deleteAnalytics(Long id);
}

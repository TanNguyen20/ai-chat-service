package com.tannguyen.ai.dto.response;

import com.tannguyen.ai.model.AnalyticsConfig;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnalyticsConfigResponseDTO {
    private Long id;

    private String hostname;

    public static AnalyticsConfigResponseDTO from(AnalyticsConfig analyticsConfig) {
        return AnalyticsConfigResponseDTO.builder()
                .id(analyticsConfig.getId())
                .hostname(analyticsConfig.getHostname())
                .build();
    }
}

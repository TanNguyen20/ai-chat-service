package com.tannguyen.ai.service.inf;

import com.tannguyen.ai.dto.response.SupersetGuestTokenResponseDTO;

public interface SupersetService {
    SupersetGuestTokenResponseDTO getGuestToken(String dashboardId, Long analyticsConfigId);
}

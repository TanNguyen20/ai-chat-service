package com.tannguyen.ai.service.inf;

import com.tannguyen.ai.dto.response.ChatbotConfigResponseDTO;

import java.util.List;

public interface ApiKeyService {
    List<ChatbotConfigResponseDTO> getListApiKeyByCurrentUser();
}

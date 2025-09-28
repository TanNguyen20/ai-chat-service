package com.tannguyen.ai.service.inf;

import com.tannguyen.ai.dto.response.ChatbotConfigResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApiKeyService {

    Page<ChatbotConfigResponseDTO> getListApiKeyByCurrentUser(Pageable pageable);
}
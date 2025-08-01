package com.tannguyen.ai.service.inf;

import com.tannguyen.ai.dto.response.ChatbotConfigResponseDTO;
import com.tannguyen.ai.model.ChatbotInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ApiKeyService {
    List<ChatbotConfigResponseDTO> getListApiKeyByCurrentUser();

    Page<ChatbotConfigResponseDTO> getListApiKeyByCurrentUserPagination(Pageable pageable);
}
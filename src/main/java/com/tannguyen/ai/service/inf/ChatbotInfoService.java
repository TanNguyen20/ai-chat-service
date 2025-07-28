package com.tannguyen.ai.service.inf;

import com.tannguyen.ai.dto.request.ChatbotCreateRequestDTO;
import com.tannguyen.ai.dto.response.ChatbotInfoResponseDTO;

public interface ChatbotInfoService {
    void createChatbot(ChatbotCreateRequestDTO request);

    ChatbotInfoResponseDTO getChatbotInfo(String encryptedKey, String host);
} 
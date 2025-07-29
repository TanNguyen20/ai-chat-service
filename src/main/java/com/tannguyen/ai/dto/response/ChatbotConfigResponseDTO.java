package com.tannguyen.ai.dto.response;

import com.tannguyen.ai.model.ChatbotInfo;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatbotConfigResponseDTO {
    private String id;
    private String name;
    private String themeColor;
    private String allowedHost;
    private String apiKey;
    private LocalDateTime createdAt;

    public static ChatbotConfigResponseDTO from(ChatbotInfo chatbotInfo, String apiKey) {
        return ChatbotConfigResponseDTO.builder()
                .id(chatbotInfo.getUuid())
                .name(chatbotInfo.getName())
                .themeColor(chatbotInfo.getThemeColor())
                .allowedHost(chatbotInfo.getAllowedHost())
                .apiKey(apiKey)
                .createdAt(chatbotInfo.getCreatedAt())
                .build();
    }
}

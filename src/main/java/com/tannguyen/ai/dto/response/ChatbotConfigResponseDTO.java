package com.tannguyen.ai.dto.response;

import com.tannguyen.ai.model.ChatbotInfo;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatbotConfigResponseDTO {
    private String name;
    private String themeColor;
    private String allowedHost;
    private String apiKey;

    public static ChatbotConfigResponseDTO from(ChatbotInfo chatbotInfo, String apiKey) {
        return ChatbotConfigResponseDTO.builder()
                .name(chatbotInfo.getName())
                .themeColor(chatbotInfo.getThemeColor())
                .allowedHost(chatbotInfo.getAllowedHost())
                .apiKey(apiKey)
                .build();
    }
}

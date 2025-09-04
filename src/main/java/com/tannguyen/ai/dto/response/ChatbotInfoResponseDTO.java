package com.tannguyen.ai.dto.response;

import com.tannguyen.ai.model.primary.ChatbotInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ChatbotInfoResponseDTO {
    private String name;
    private String themeColor;

    public static ChatbotInfoResponseDTO from(ChatbotInfo chatbotInfo) {
        return ChatbotInfoResponseDTO.builder()
                .name(chatbotInfo.getName())
                .themeColor(chatbotInfo.getThemeColor())
                .build();
    }
}



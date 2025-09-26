package com.tannguyen.ai.dto.request;

import lombok.Data;

@Data
public class ChatbotUpdateRequestDTO {
    private String name;
    private String allowedHost;
    private String themeColor;
}

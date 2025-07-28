package com.tannguyen.ai.dto.request;

import lombok.Data;

@Data
public class ChatbotCreateRequestDTO {
    private String name;
    private String allowedHost;
    private String themeColor;
}

package com.tannguyen.ai.controller;

import com.tannguyen.ai.dto.request.ChatbotCreateRequestDTO;
import com.tannguyen.ai.dto.response.ChatbotConfigResponseDTO;
import com.tannguyen.ai.dto.response.ChatbotInfoResponseDTO;
import com.tannguyen.ai.service.inf.ApiKeyService;
import com.tannguyen.ai.service.inf.ChatbotInfoService;
import com.tannguyen.ai.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.tannguyen.ai.constant.CommonConstant.API_V1;

@RestController
@RequestMapping(API_V1 + "/chatbot")
@AllArgsConstructor
public class ChatbotController {

    private final ApiKeyService apiKeyService;
    private final ChatbotInfoService chatbotInfoService;

    @GetMapping("/info")
    public ResponseEntity<?> getChatbotInfo(
            @RequestHeader("X-Api-Key") String apiKey,
            HttpServletRequest request
    ) {
        String uiHost = CommonUtil.extractUiHost(request);

        ChatbotInfoResponseDTO chatbotInfoResponseDTO = chatbotInfoService.getChatbotInfo(apiKey, uiHost);

        if (chatbotInfoResponseDTO == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid API Key or unauthorized host");
        }

        return ResponseEntity.ok(chatbotInfoResponseDTO);
    }

    @GetMapping("/config-info")
    public ResponseEntity<?> getChatbotConfigInfo() {
        List<ChatbotConfigResponseDTO> chatbotConfigResponseDTOList = apiKeyService.getListApiKeyByCurrentUser();
        return ResponseEntity.ok(chatbotConfigResponseDTOList);
    }

    @PostMapping
    public ResponseEntity<?> createChatbot(@RequestBody ChatbotCreateRequestDTO request) {
        chatbotInfoService.createChatbot(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteChatbot(@PathVariable String uuid) {
        chatbotInfoService.deleteChatbot(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

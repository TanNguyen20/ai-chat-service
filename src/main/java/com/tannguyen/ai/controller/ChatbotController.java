package com.tannguyen.ai.controller;

import com.tannguyen.ai.dto.request.ChatbotCreateRequestDTO;
import com.tannguyen.ai.dto.request.ChatbotUpdateRequestDTO;
import com.tannguyen.ai.dto.response.ChatbotConfigResponseDTO;
import com.tannguyen.ai.dto.response.ChatbotInfoResponseDTO;
import com.tannguyen.ai.dto.response.ResponseFactory;
import com.tannguyen.ai.service.inf.ApiKeyService;
import com.tannguyen.ai.service.inf.ChatbotInfoService;
import com.tannguyen.ai.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

        return ResponseFactory.success(chatbotInfoResponseDTO, "Get chatbot info successfully", HttpStatus.OK);
    }

    @GetMapping("/config-info")
    public ResponseEntity<?> getChatbotConfigInfo() {
        List<ChatbotConfigResponseDTO> chatbotConfigResponseDTOList = apiKeyService.getListApiKeyByCurrentUser();
        return ResponseFactory.success(chatbotConfigResponseDTOList, "Get chatbot config info list successfully", HttpStatus.OK);
    }

    @GetMapping("/config-info-pagination")
    public ResponseEntity<?> getChatbotConfigInfoPagination(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<ChatbotConfigResponseDTO> chatbotConfigResponseDTOList = apiKeyService.getListApiKeyByCurrentUserPagination(pageable);
        return ResponseFactory.success(chatbotConfigResponseDTOList, "Get chatbot config info list successfully", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createChatbot(@RequestBody ChatbotCreateRequestDTO request) {
        chatbotInfoService.createChatbot(request);
        return ResponseFactory.success(null, "Create chatbot successfully", HttpStatus.CREATED);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<?> updateChatbot(@PathVariable String uuid,
                                           @RequestBody ChatbotUpdateRequestDTO request) {
        chatbotInfoService.updateChatbot(uuid, request);
        return ResponseFactory.success(null, "Update chatbot successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteChatbot(@PathVariable String uuid) {
        chatbotInfoService.deleteChatbot(uuid);
        return ResponseFactory.success(null, "Delete chatbot successfully", HttpStatus.NO_CONTENT);
    }
}

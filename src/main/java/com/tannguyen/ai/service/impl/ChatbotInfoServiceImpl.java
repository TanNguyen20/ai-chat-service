package com.tannguyen.ai.service.impl;

import com.tannguyen.ai.dto.request.ChatbotCreateRequestDTO;
import com.tannguyen.ai.dto.response.ChatbotInfoResponseDTO;
import com.tannguyen.ai.exception.NotFoundException;
import com.tannguyen.ai.model.ChatbotInfo;
import com.tannguyen.ai.model.User;
import com.tannguyen.ai.repository.ChatbotInfoRepository;
import com.tannguyen.ai.repository.UserRepository;
import com.tannguyen.ai.service.inf.ChatbotInfoService;
import com.tannguyen.ai.util.AESUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChatbotInfoServiceImpl implements ChatbotInfoService {

    private final ChatbotInfoRepository chatbotInfoRepository;
    private final UserRepository userRepository;

    @Override
    public ChatbotInfoResponseDTO getChatbotInfo(String encryptedKey, String host) {
        try {
            String decrypted = AESUtil.decrypt(encryptedKey);
            String[] parts = decrypted.split("::");
            String uuid = parts[0];

            ChatbotInfo chatbotInfo = chatbotInfoRepository.findById(uuid).orElseThrow(() ->
                    new NotFoundException("Chatbot with ID " + uuid + " not found"));

            if (!chatbotInfo.getAllowedHost().contains(host)) {
                throw new NotFoundException("Chatbot with ID " + uuid + " not found");
            }

            return ChatbotInfoResponseDTO.from(chatbotInfo);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void createChatbot(ChatbotCreateRequestDTO request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));

        ChatbotInfo chatbotInfo = ChatbotInfo.builder()
                .name(request.getName())
                .allowedHost(request.getAllowedHost())
                .themeColor(request.getThemeColor())
                .user(user)
                .build();
        chatbotInfoRepository.save(chatbotInfo);
    }

    @Override
    public void deleteChatbot(String uuid) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));

        ChatbotInfo chatbotInfo = chatbotInfoRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Chatbot with ID " + uuid + " not found"));

        // Check if the current user is the owner of the chatbot
        if (!chatbotInfo.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You don't have permission to delete this chatbot");
        }

        chatbotInfoRepository.delete(chatbotInfo);
    }
}

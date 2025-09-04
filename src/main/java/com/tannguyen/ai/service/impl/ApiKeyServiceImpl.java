package com.tannguyen.ai.service.impl;

import com.tannguyen.ai.dto.response.ChatbotConfigResponseDTO;
import com.tannguyen.ai.exception.NotFoundException;
import com.tannguyen.ai.model.primary.ChatbotInfo;
import com.tannguyen.ai.repository.primary.ChatbotInfoRepository;
import com.tannguyen.ai.repository.primary.UserRepository;
import com.tannguyen.ai.service.inf.ApiKeyService;
import com.tannguyen.ai.util.AESUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ApiKeyServiceImpl implements ApiKeyService {

    private final ChatbotInfoRepository chatbotInfoRepository;
    private final UserRepository userRepository;

    @Override
    public List<ChatbotConfigResponseDTO> getListApiKeyByCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Long userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found")).getId();

        List<ChatbotInfo> chatbotInfoList = chatbotInfoRepository.findByUserId(userId);

        return chatbotInfoList.stream().map((item) -> {
            String apiKey = AESUtil.encrypt(item.getUuid());
            return ChatbotConfigResponseDTO.from(item, apiKey);
        }).toList();
    }

    @Override
    public Page<ChatbotConfigResponseDTO> getListApiKeyByCurrentUserPagination(Pageable pageable) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Long userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found")).getId();

        Page<ChatbotInfo> chatbotInfos = chatbotInfoRepository.findByUserId(userId, pageable);

        return chatbotInfos.map(item -> {
            String apiKey = AESUtil.encrypt(item.getUuid());
            return ChatbotConfigResponseDTO.from(item, apiKey);
        });
    }
}

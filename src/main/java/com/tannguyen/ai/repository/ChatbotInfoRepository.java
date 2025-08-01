package com.tannguyen.ai.repository;

import com.tannguyen.ai.model.ChatbotInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatbotInfoRepository extends JpaRepository<ChatbotInfo, String> {
    List<ChatbotInfo> findByUserId(Long userId);

    Page<ChatbotInfo> findByUserId(Long userId, Pageable pageable);
}
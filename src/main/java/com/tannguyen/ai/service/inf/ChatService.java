package com.tannguyen.ai.service.inf;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

public interface ChatService {
    Flux<ServerSentEvent<String>> uploadAndAsk(MultipartFile file, String question);
}

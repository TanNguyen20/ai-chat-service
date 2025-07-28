package com.tannguyen.ai.controller;

import com.tannguyen.ai.service.inf.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import static com.tannguyen.ai.constant.CommonConstant.API_V1;

@RestController
@RequestMapping(API_V1 + "/chat")
@AllArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping(value = "/upload-and-ask", produces = "text/event-stream")
    public Flux<ServerSentEvent<String>> uploadAndAsk(
            @RequestPart("file") MultipartFile file,
            @RequestPart("question") String question
    ) {
        return chatService.uploadAndAsk(file, question);
    }
}

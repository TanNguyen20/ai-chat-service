package com.tannguyen.ai.service.impl;

import com.tannguyen.ai.service.inf.ChatService;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;

@Service
public class ChatServiceImpl implements ChatService {
    private final WebClient webClient;

    public ChatServiceImpl() {
        HttpClient httpClient = HttpClient.create().compress(true);
        ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8000")
                .clientConnector(connector)
                .build();
    }

    @Override
    public Flux<ServerSentEvent<String>> uploadAndAsk(MultipartFile file, String question) {
        return webClient.post()
                .uri("/upload-and-ask")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .body(BodyInserters.fromMultipartData("file", file.getResource())
                        .with("question", question))
                .retrieve()
                .bodyToFlux(String.class)
                .map(data -> ServerSentEvent.builder(data).build());
    }
}

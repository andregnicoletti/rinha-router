package com.nicoletti.rinharouter.controller;

import com.nicoletti.rinharouter.dto.ExtractResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
public class ExtratoHandler {

    public Mono<ServerResponse> getExtrato(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");

        // Dados mockados para teste
        ExtractResponse response = new ExtractResponse(
                1000, // saldo
                LocalDateTime.now(), // data do extrato
                5000 // limite
        );

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response);
    }

}

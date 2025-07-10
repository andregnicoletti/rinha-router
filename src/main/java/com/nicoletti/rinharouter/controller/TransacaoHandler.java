package com.nicoletti.rinharouter.controller;

import com.nicoletti.rinharouter.dto.TransacaoRequest;
import com.nicoletti.rinharouter.dto.TransacaoResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class TransacaoHandler {

    public Mono<ServerResponse> processarTransacao(ServerRequest request) {
        String clienteId = request.pathVariable("id");

        return request.bodyToMono(TransacaoRequest.class).flatMap(transacao -> {
            // Aqui você faria: buscar cliente, validar limite, atualizar saldo, salvar transação...
            // Vamos simular uma resposta por enquanto:

            TransacaoResponse response = new TransacaoResponse(5000, 1200); // mock
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(response);
        });

    }

}

package com.nicoletti.rinharouter.route;

import com.nicoletti.rinharouter.controller.TransacaoHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class TransacaoRouter {

    @Bean
    public RouterFunction<ServerResponse> routeTransacao(TransacaoHandler handler) {
        return RouterFunctions.route(
                RequestPredicates.POST("/clientes/{id}/transacoes"), handler::processarTransacao);
    }

}

package com.nicoletti.rinharouter.route;

import com.nicoletti.rinharouter.controller.ExtratoHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Controller
public class ExtratoRouter {

    @Bean
    public RouterFunction<ServerResponse> routeExtrato(ExtratoHandler handler) {
        return RouterFunctions.route(RequestPredicates.GET("clientes/{id}/extrato"), handler::getExtrato);
    }

}

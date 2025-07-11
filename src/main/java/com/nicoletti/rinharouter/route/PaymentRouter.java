package com.nicoletti.rinharouter.route;

import com.nicoletti.rinharouter.controller.PaymentHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class PaymentRouter {

    @Bean
    public RouterFunction<ServerResponse> routePayment(PaymentHandler handler) {
        return RouterFunctions.route(
                RequestPredicates.POST("/payments"), handler::processPayment);
    }

}

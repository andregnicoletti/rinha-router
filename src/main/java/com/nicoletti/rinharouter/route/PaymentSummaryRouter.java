package com.nicoletti.rinharouter.route;

import com.nicoletti.rinharouter.controller.PaymentSummaryHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class PaymentSummaryRouter {

    @Bean
    public RouterFunction<?> summaryRoutes(PaymentSummaryHandler handler) {
        return route(GET("/payments-summary"), handler::getSummary);
    }
}
package com.nicoletti.rinharouter.service.impl;

import com.nicoletti.rinharouter.dto.PaymentHealthResponse;
import com.nicoletti.rinharouter.service.api.AnalyzerStatusService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class AnalyzerStatusServiceImpl implements AnalyzerStatusService {

    private static final Logger logger = LoggerFactory.getLogger(AnalyzerStatusServiceImpl.class);

    private final WebClient webClient = WebClient.create();

    private AtomicReference<String> activeBaseUrl;

    private static String SERVICE_HEALTH_PATH = "/payments/service-health";

    @Value("${payment.service.url.default}")
    private String DEFAULT_URL;

    @Value("${payment.service.url.fallback}")
    private String FALLBACK_URL;

    @PostConstruct
    public void init() {
        startHealthCheckLoop();
        activeBaseUrl = new AtomicReference<>(DEFAULT_URL);
    }

    private void startHealthCheckLoop() {
        Flux.interval(Duration.ZERO, Duration.ofSeconds(5))
                .flatMap(tick -> checkStatus())
                .subscribe();
    }

    public Mono<Void> checkStatus() {

        return webClient.get()
                .uri(DEFAULT_URL + SERVICE_HEALTH_PATH) // endpoint de monitoramento
                .retrieve()
                .bodyToMono(PaymentHealthResponse.class)
                .doOnNext(health -> {
                    boolean failing = health.falling();
                    if (failing) {
                        activeBaseUrl.set(FALLBACK_URL);
                        logger.warn("Fallback ativado.");
                    } else {
                        activeBaseUrl.set(DEFAULT_URL);
                        logger.info("Serviço saudável. Usando default.");
                    }
                }).then(); // retorna Mono<Void>
    }

    public String getCurrentBaseUrl() {
        return activeBaseUrl.get();
    }
}

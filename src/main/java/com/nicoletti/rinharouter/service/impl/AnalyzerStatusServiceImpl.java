package com.nicoletti.rinharouter.service.impl;

import com.nicoletti.rinharouter.dto.PaymentHealthResponse;
import com.nicoletti.rinharouter.dto.ServiceStatusDTO;
import com.nicoletti.rinharouter.service.api.AnalyzerStatusService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class AnalyzerStatusServiceImpl implements AnalyzerStatusService {

    private static final Logger logger = LoggerFactory.getLogger(AnalyzerStatusServiceImpl.class);

    private final WebClient webClient = WebClient.create();

    private static String SERVICE_HEALTH_PATH = "/payments/service-health";

    private String serviceName;

    private ServiceStatusCacheImpl serviceStatusCache;

    @Value("${payment.service.url.default}")
    private String DEFAULT_URL;

    @Value("${payment.service.url.fallback}")
    private String FALLBACK_URL;

    public AnalyzerStatusServiceImpl(ServiceStatusCacheImpl serviceStatusCache) {
        this.serviceStatusCache = serviceStatusCache;
    }

    @PostConstruct
    public void init() {
        startHealthCheckLoop();
    }

    private void startHealthCheckLoop() {
        Flux.interval(Duration.ZERO, Duration.ofSeconds(5))
                .flatMap(tick -> checkStatus())
                .subscribe();
    }

    public Mono<Void> checkStatus() {

        ServiceStatusDTO serviceStatus = this.getServiceStatus();
        if (serviceStatus == null || serviceStatus.lastUpdated() == null ||
                System.currentTimeMillis() - serviceStatus.lastUpdated() >= 5000) {
            logger.info("Service status is null or outdated, checking health.");

            // Inicia uma chamada GET usando o WebClient
            return webClient.get()
                    // Define a URI do endpoint que será chamado
                    .uri(DEFAULT_URL + SERVICE_HEALTH_PATH)

                    // Envia a requisição e prepara para processar a resposta
                    .retrieve()

                    // Converte o corpo da resposta para um objeto PaymentHealthResponse, de forma reativa (Mono)
                    .bodyToMono(PaymentHealthResponse.class)

                    // Executa esta ação sempre que uma resposta válida for recebida
                    .doOnNext(health -> {
                        // Verifica se o serviço está "failing" (com problema)
                        boolean failing = health.falling();
                        if (failing) {
                            // Se estiver com problema, ativa o fallback
                            logger.warn("Fallback ativado.");
                            serviceStatusCache.setStatus(new ServiceStatusDTO("fallback", FALLBACK_URL, System.currentTimeMillis()));
                        } else {
                            // Se estiver saudável, mantém o default
                            logger.info("Serviço saudável. Usando default.");
                            serviceStatusCache.setStatus(new ServiceStatusDTO("default", DEFAULT_URL, System.currentTimeMillis()));
                        }
                    })

                    // Caso aconteça qualquer erro (exceção) em qualquer etapa anterior, executa este bloco
                    .onErrorResume(ex -> {
                        // Verifica se o erro é uma exceção do tipo WebClientResponseException (exceção de resposta HTTP)
                        // e se o status HTTP é 429 (Too Many Requests)
                        if (ex instanceof WebClientResponseException wcre && wcre.getStatusCode().value() == 429) {
                            // Se for 429, não faz nada
                            logger.warn("Recebeu 429 Too Many Requests, ativando fallback. {}", wcre.getMessage());
                        } else {
                            // Para outros erros, loga o erro e ativa o fallback padrão
                            logger.error("Erro ao verificar status, ativando fallback automaticamente. {}", ex.getMessage());
                            serviceStatusCache.setStatus(new ServiceStatusDTO("default", DEFAULT_URL, System.currentTimeMillis()));
                        }
                        // Retorna Mono.empty() para "engolir" o erro e terminar o fluxo normalmente
                        return Mono.empty();
                    })

                    // Indica que, independentemente do que acontecer, será retornado Mono<Void> (sem valor, só o sinal)
                    .then();

        } else {
            logger.info("Service status is up-to-date, skipping health check.");
            return Mono.empty(); // Se o status está atualizado, não faz nada
        }
    }


    @Override
    public ServiceStatusDTO getServiceStatus() {
        ServiceStatusDTO urlService = serviceStatusCache.getStatus();
        if (urlService == null) {
            logger.warn("ServiceStatusCache is empty, returning default URL.");
            urlService = new ServiceStatusDTO("fallback", FALLBACK_URL, null);
        }
        return urlService;
    }

}

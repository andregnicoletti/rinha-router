package com.nicoletti.rinharouter.service.api;

import reactor.core.publisher.Mono;

public interface AnalyzerStatusService {

    Mono<Void> checkStatus();

    String getCurrentBaseUrl();
}

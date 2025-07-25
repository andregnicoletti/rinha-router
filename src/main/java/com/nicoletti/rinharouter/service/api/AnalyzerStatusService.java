package com.nicoletti.rinharouter.service.api;

import com.nicoletti.rinharouter.dto.ServiceStatusDTO;
import reactor.core.publisher.Mono;

public interface AnalyzerStatusService {

    Mono<Void> checkStatus();

    ServiceStatusDTO getServiceStatus();

}

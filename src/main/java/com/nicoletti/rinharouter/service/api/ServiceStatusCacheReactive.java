package com.nicoletti.rinharouter.service.api;

import com.nicoletti.rinharouter.dto.ServiceStatusDTO;
import reactor.core.publisher.Mono;

public interface ServiceStatusCacheReactive {

    Mono<Boolean> setStatus(ServiceStatusDTO dto);

    Mono<ServiceStatusDTO> getStatus();

}

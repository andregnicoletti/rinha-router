package com.nicoletti.rinharouter.dto;

import java.time.LocalDateTime;

public record ServiceStatusDTO(
        String serviceName,
        String baseUrl,
        Long lastUpdated
) {
}

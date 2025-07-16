package com.nicoletti.rinharouter.service.api;

import com.nicoletti.rinharouter.dto.ServiceStatusDTO;

public interface ServiceStatusCache {

    void setStatus(ServiceStatusDTO dto);

    ServiceStatusDTO getStatus();

}

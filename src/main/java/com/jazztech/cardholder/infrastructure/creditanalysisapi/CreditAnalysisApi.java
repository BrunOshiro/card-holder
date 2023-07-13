package com.jazztech.cardholder.infrastructure.creditanalysisapi;

import com.jazztech.cardholder.infrastructure.creditanalysisapi.dto.CreditAnalysisDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "credit-analysis", url = "http://172.21.0.6:8081", configuration = CustomFeignConfiguration.class)
public interface CreditAnalysisApi {
    @GetMapping("/api/v1/credits/analysis/{id}")
    CreditAnalysisDto getCreditAnalysisId(
            @PathVariable("id") UUID creditAnalysisId
    );
}

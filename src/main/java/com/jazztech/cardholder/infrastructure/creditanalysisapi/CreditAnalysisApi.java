package com.jazztech.cardholder.infrastructure.creditanalysisapi;

import com.jazztech.cardholder.infrastructure.creditanalysisapi.dto.CreditAnalysisDto;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "credit-analysis", url = "http://localhost:8081")
public interface CreditAnalysisApi {
    @GetMapping("/api/v1/credits/analysis")
    List<CreditAnalysisDto> getCreditAnalysisByClientId(
            @RequestParam(value = "clientId", required = false) UUID clientId
    );
}

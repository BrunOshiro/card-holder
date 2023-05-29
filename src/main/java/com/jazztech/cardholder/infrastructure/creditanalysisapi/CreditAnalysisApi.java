package com.jazztech.cardholder.infrastructure.creditanalysisapi;

import com.jazztech.cardholder.infrastructure.creditanalysisapi.dto.CreditAnalysisDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "credit-analysis", url = "http://localhost:8082")
public interface CreditAnalysisApi {
    @GetMapping("api/v.1.0.0/credit/analysis/")
    CreditAnalysisDto getCreditAnalysisByCpfOrClientId(
            @RequestParam(value = "clientId", required = false) UUID clientId,
            @RequestParam(value = "cpf") @Validated String cpf
    );
}

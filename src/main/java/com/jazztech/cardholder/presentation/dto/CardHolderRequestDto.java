package com.jazztech.cardholder.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CardHolderRequestDto(
        UUID clientId,
        UUID creditAnalysisId,
        @Nullable
        BankAccount bankAccount
) {
    public boolean isBankAccountValid() {
        final Boolean isBankAccountGroupFilled = bankAccount == null;
        final Boolean isBankAccountAttributesFilled = bankAccount != null
                && bankAccount.account() != null
                && bankAccount.agency() != null
                && bankAccount.bankCode() != null;
        return isBankAccountGroupFilled || isBankAccountAttributesFilled;
    }

    @Builder(toBuilder = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record BankAccount(

            @Size(min = 6, max = 10, message = "Account must be between 6 and 10 characters")
            String account,
            @Size(min = 4, max = 5, message = "Agency must be between 4 and 5 characters")
            String agency,
            @Size(min = 3, max = 3, message = "Bank Code must be 3 characters")
            String bankCode
    ) {
    }
}

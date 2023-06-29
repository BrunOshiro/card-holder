package com.jazztech.cardholder.presentation.handler;

import com.jazztech.cardholder.infrastructure.handler.exception.CardHolderAlreadyExists;
import com.jazztech.cardholder.infrastructure.handler.exception.CardHolderInactive;
import com.jazztech.cardholder.infrastructure.handler.exception.CardHolderNotFound;
import com.jazztech.cardholder.infrastructure.handler.exception.CardNotFound;
import com.jazztech.cardholder.infrastructure.handler.exception.CreditAnalysisNotApproved;
import com.jazztech.cardholder.infrastructure.handler.exception.CreditAnalysisNotFound;
import com.jazztech.cardholder.infrastructure.handler.exception.CreditLimitNotAvailable;
import com.jazztech.cardholder.infrastructure.handler.exception.InvalidCardHolderStatusEnum;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {
    private ProblemDetail problemDetailBuilder(HttpStatus status, String title, String message, Exception e) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setType(URI.create("https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status/" + status.value()));
        problemDetail.setTitle(title);
        problemDetail.setDetail(message);
        return problemDetail;
    }

    @ExceptionHandler(CreditAnalysisNotFound.class)
    public ResponseEntity<ProblemDetail> creditAnalysisNotFoundHandler(CreditAnalysisNotFound e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.NOT_FOUND, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(CardHolderAlreadyExists.class)
    public ResponseEntity<ProblemDetail> cardHolderAlreadyExistsHandler(CardHolderAlreadyExists e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.UNPROCESSABLE_ENTITY, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(CreditAnalysisNotApproved.class)
    public ResponseEntity<ProblemDetail> creditAnalysisNotApprovedHandler(CreditAnalysisNotApproved e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.BAD_REQUEST, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(CardHolderNotFound.class)
    public ResponseEntity<ProblemDetail> cardHolderNotFoundHandler(CardHolderNotFound e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.NOT_FOUND, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(InvalidCardHolderStatusEnum.class)
    public ResponseEntity<ProblemDetail> invalidCardHolderStatusEnumHandler(InvalidCardHolderStatusEnum e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.BAD_REQUEST, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(CreditLimitNotAvailable.class)
    public ResponseEntity<ProblemDetail> creditLimitNotAvailableHandler(CreditLimitNotAvailable e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.PRECONDITION_FAILED, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(CardHolderInactive.class)
    public ResponseEntity<ProblemDetail> cardHolderInactiveHandler(CardHolderInactive e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.PRECONDITION_FAILED, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }

    @ExceptionHandler(CardNotFound.class)
    public ResponseEntity<ProblemDetail> cardNotFoundHandler(CardNotFound e) {
        final ProblemDetail problemDetail = problemDetailBuilder(
                HttpStatus.NOT_FOUND, e.getClass().getSimpleName(),
                e.getMessage(), e);
        return ResponseEntity.status(problemDetail.getStatus())
                .body(problemDetail
                );
    }
}

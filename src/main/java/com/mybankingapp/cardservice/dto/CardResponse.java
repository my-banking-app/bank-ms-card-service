package com.mybankingapp.cardservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CardResponse {
    private UUID id;
    private UUID accountId;
    private String cardNumber;
    private String cardType;
    private String status;
    private LocalDateTime issueDate;
    private LocalDateTime expirationDate;
    private BigDecimal spendingLimit;
}
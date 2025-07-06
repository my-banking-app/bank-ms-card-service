package com.mybankingapp.cardservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateCardRequest {
    private UUID accountId;
    private String cardType;
    private BigDecimal spendingLimit;
}
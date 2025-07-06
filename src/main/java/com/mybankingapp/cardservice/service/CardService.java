package com.mybankingapp.cardservice.service;

import com.mybankingapp.cardservice.dto.CreateCardRequest;
import com.mybankingapp.cardservice.dto.CardResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface CardService {
    CardResponse createCard(CreateCardRequest request);
    List<CardResponse> getCardsByAccount(UUID accountId);
    CardResponse blockCard(UUID cardId);
    CardResponse updateSpendingLimit(UUID cardId, BigDecimal newLimit);
}


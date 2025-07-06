package com.mybankingapp.cardservice.service.impl;

import com.mybankingapp.cardservice.dto.*;
import com.mybankingapp.cardservice.model.Card;
import com.mybankingapp.cardservice.repository.CardRepository;
import com.mybankingapp.cardservice.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository repository;

    @Override
    public CardResponse createCard(CreateCardRequest request) {
        Card card = new Card();
        card.setAccountId(request.getAccountId());
        card.setCardNumber(generateMaskedCardNumber());
        card.setCardType(request.getCardType());
        card.setStatus("ACTIVE");
        card.setIssueDate(LocalDateTime.now());
        card.setExpirationDate(LocalDateTime.now().plusYears(5));
        card.setSpendingLimit(request.getSpendingLimit());
        card.setCreatedAt(LocalDateTime.now());
        card.setUpdatedAt(LocalDateTime.now());
        Card saved = repository.save(card);
        return mapToResponse(saved);
    }

    @Override
    public List<CardResponse> getCardsByAccount(UUID accountId) {
        return repository.findByAccountId(accountId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CardResponse blockCard(UUID cardId) {
        Card card = repository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        card.setStatus("BLOCKED");
        card.setUpdatedAt(LocalDateTime.now());
        return mapToResponse(repository.save(card));
    }

    @Override
    public CardResponse updateSpendingLimit(UUID cardId, BigDecimal newLimit) {
        Card card = repository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        card.setSpendingLimit(newLimit);
        card.setUpdatedAt(LocalDateTime.now());
        return mapToResponse(repository.save(card));
    }

    private CardResponse mapToResponse(Card card) {
        CardResponse response = new CardResponse();
        BeanUtils.copyProperties(card, response);
        return response;
    }

    private String generateMaskedCardNumber() {
        return "XXXX-XXXX-XXXX-" + (int)(Math.random() * 10000);
    }
}

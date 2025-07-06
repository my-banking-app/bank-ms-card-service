package com.mybankingapp.cardservice.controller;

import com.mybankingapp.cardservice.dto.*;
import com.mybankingapp.cardservice.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping("/create")
    public ResponseEntity<CardResponse> createCard(@RequestBody CreateCardRequest request) {
        return ResponseEntity.ok(cardService.createCard(request));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<CardResponse>> getCardsByAccount(@PathVariable UUID accountId) {
        return ResponseEntity.ok(cardService.getCardsByAccount(accountId));
    }

    @PutMapping("/{cardId}/block")
    public ResponseEntity<CardResponse> blockCard(@PathVariable UUID cardId) {
        return ResponseEntity.ok(cardService.blockCard(cardId));
    }

    @PutMapping("/{cardId}/limit")
    public ResponseEntity<CardResponse> updateLimit(@PathVariable UUID cardId, @RequestParam BigDecimal newLimit) {
        return ResponseEntity.ok(cardService.updateSpendingLimit(cardId, newLimit));
    }
}

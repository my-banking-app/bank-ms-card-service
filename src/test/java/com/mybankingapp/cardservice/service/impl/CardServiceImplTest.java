package com.mybankingapp.cardservice.service.impl;

import com.mybankingapp.cardservice.dto.CardResponse;
import com.mybankingapp.cardservice.dto.CreateCardRequest;
import com.mybankingapp.cardservice.model.Card;
import com.mybankingapp.cardservice.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CardServiceImplTest {

    private CardRepository repository;
    private CardServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(CardRepository.class);
        service = new CardServiceImpl(repository);
    }

    @Test
    void testCreateCard_ShouldCreateCardSuccessfully() {
        CreateCardRequest request = new CreateCardRequest();
        request.setAccountId(UUID.randomUUID());
        request.setCardType("CREDIT");
        request.setSpendingLimit(BigDecimal.valueOf(100000));

        ArgumentCaptor<Card> cardCaptor = ArgumentCaptor.forClass(Card.class);
        when(repository.save(any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CardResponse response = service.createCard(request);

        verify(repository).save(cardCaptor.capture());
        Card savedCard = cardCaptor.getValue();

        assertThat(savedCard.getAccountId()).isEqualTo(request.getAccountId());
        assertThat(savedCard.getCardType()).isEqualTo("CREDIT");
        assertThat(savedCard.getSpendingLimit()).isEqualByComparingTo("100000");
        assertThat(savedCard.getCardNumber()).matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}");

        assertThat(response).isNotNull();
        assertThat(response.getCardNumber()).isEqualTo(savedCard.getCardNumber());
    }

    @Test
    void testGetCardsByAccount_ShouldReturnCards() {
        UUID accountId = UUID.randomUUID();

        Card card = new Card();
        card.setAccountId(accountId);
        card.setCardNumber("1234-5678-1234-5678");

        when(repository.findByAccountId(accountId)).thenReturn(List.of(card));

        List<CardResponse> responses = service.getCardsByAccount(accountId);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getCardNumber()).isEqualTo("1234-5678-1234-5678");
    }

    @Test
    void testBlockCard_ShouldUpdateCardStatus() {
        UUID cardId = UUID.randomUUID();
        Card card = new Card();
        card.setId(cardId);
        card.setStatus("ACTIVE");

        when(repository.findById(cardId)).thenReturn(Optional.of(card));
        when(repository.save(any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CardResponse response = service.blockCard(cardId);

        assertThat(response.getStatus()).isEqualTo("BLOCKED");
    }

    @Test
    void testBlockCard_ShouldThrowIfCardNotFound() {
        UUID cardId = UUID.randomUUID();

        when(repository.findById(cardId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.blockCard(cardId));
        assertThat(ex.getMessage()).isEqualTo("Card not found");
    }

    @Test
    void testUpdateSpendingLimit_ShouldUpdateLimit() {
        UUID cardId = UUID.randomUUID();
        Card card = new Card();
        card.setId(cardId);
        card.setSpendingLimit(BigDecimal.valueOf(50000));

        when(repository.findById(cardId)).thenReturn(Optional.of(card));
        when(repository.save(any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CardResponse response = service.updateSpendingLimit(cardId, BigDecimal.valueOf(200000));

        assertThat(response.getSpendingLimit()).isEqualByComparingTo("200000");
    }

    @Test
    void testUpdateSpendingLimit_ShouldThrowIfCardNotFound() {
        UUID cardId = UUID.randomUUID();

        when(repository.findById(cardId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.updateSpendingLimit(cardId, BigDecimal.TEN));
        assertThat(ex.getMessage()).isEqualTo("Card not found");
    }
}

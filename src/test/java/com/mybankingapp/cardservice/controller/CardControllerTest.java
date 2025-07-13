package com.mybankingapp.cardservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybankingapp.cardservice.dto.*;
import com.mybankingapp.cardservice.service.CardService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CardController.class)
@AutoConfigureMockMvc(addFilters = false)
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CardService cardService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateCard() throws Exception {
        CreateCardRequest request = new CreateCardRequest();
        request.setAccountId(UUID.randomUUID());
        request.setCardType("CREDIT");
        request.setSpendingLimit(BigDecimal.valueOf(150000));

        CardResponse response = new CardResponse();
        response.setCardNumber("1234-5678-1234-5678");
        response.setCardType("CREDIT");
        response.setSpendingLimit(BigDecimal.valueOf(150000));
        response.setStatus("ACTIVE");

        when(cardService.createCard(any(CreateCardRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/cards/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardNumber").value("1234-5678-1234-5678"))
                .andExpect(jsonPath("$.cardType").value("CREDIT"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void testGetCardsByAccount() throws Exception {
        UUID accountId = UUID.randomUUID();

        CardResponse card = new CardResponse();
        card.setCardNumber("9876-4321-9876-4321");
        card.setCardType("DEBIT");

        when(cardService.getCardsByAccount(accountId)).thenReturn(List.of(card));

        mockMvc.perform(get("/api/v1/cards/account/" + accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cardNumber").value("9876-4321-9876-4321"))
                .andExpect(jsonPath("$[0].cardType").value("DEBIT"));
    }

    @Test
    void testBlockCard() throws Exception {
        UUID cardId = UUID.randomUUID();

        CardResponse card = new CardResponse();
        card.setCardNumber("0000-1111-2222-3333");
        card.setStatus("BLOCKED");

        when(cardService.blockCard(cardId)).thenReturn(card);

        mockMvc.perform(put("/api/v1/cards/" + cardId + "/block"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("BLOCKED"));
    }

    @Test
    void testUpdateLimit() throws Exception {
        UUID cardId = UUID.randomUUID();
        BigDecimal newLimit = BigDecimal.valueOf(999000);

        CardResponse card = new CardResponse();
        card.setCardNumber("4321-4321-4321-4321");
        card.setSpendingLimit(newLimit);

        when(cardService.updateSpendingLimit(Mockito.eq(cardId), Mockito.eq(newLimit)))
                .thenReturn(card);

        mockMvc.perform(put("/api/v1/cards/" + cardId + "/limit")
                        .param("newLimit", newLimit.toPlainString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.spendingLimit").value(newLimit.intValue()));
    }
}

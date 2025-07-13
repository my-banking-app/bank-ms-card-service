package com.mybankingapp.cardservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "cards")
@AllArgsConstructor @NoArgsConstructor
public class Card {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID accountId;

    @Column(nullable = false, unique = true)
    private String cardNumber;

    @Column(nullable = false)
    private String cardType;

    @Column(nullable = false)
    private String status;

    private LocalDateTime issueDate;
    private LocalDateTime expirationDate;
    private BigDecimal spendingLimit;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

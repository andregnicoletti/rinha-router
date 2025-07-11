package com.nicoletti.rinharouter.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payment_transactions")
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String correlationId;

    private String endpointType;

    private double amount;

    private LocalDateTime processedAt;

}
package com.tickevent.app.domain.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Order {
    private enum PaymentStatus {
        PENDING,
        APPROVED,
        REJECTED,
        REFUNDED,
        CANCELED
    }

    private UUID id;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private PaymentStatus paymentStatus;
    private String paymentMethod;
    private String gatewayTransactionId;
    private User ticketHolder;
    private List<Ticket> tickets;
}

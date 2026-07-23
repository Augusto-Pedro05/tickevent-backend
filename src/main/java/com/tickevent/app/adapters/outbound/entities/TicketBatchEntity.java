package com.tickevent.app.adapters.outbound.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ticket_batches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketBatchEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "batch_number", nullable = false)
    private Integer batchNumber;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "total_limit", nullable = false)
    private Integer totalLimit;

    @Column(name = "available_quantity", nullable = false)
    private Integer availableQuantity;

    @Column(name = "sales_start_date", nullable = false)
    private LocalDateTime salesStartDate;

    @Column(name = "sales_end_date", nullable = false)
    private LocalDateTime salesEndDate;

    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private TicketCategoryEntity category;
}
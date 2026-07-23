package com.tickevent.app.domain.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TicketCategory {

    @Setter(AccessLevel.NONE)
    @NotNull(message = "Ticket category ID is required.")
    private UUID id;

    @NotBlank(message = "Category name is required.")
    private String name;

    @NotBlank(message = "Category description is required.")
    private String description;

    @NotNull(message = "Batches list cannot be null.")
    private List<TicketBatch> batches;

    public TicketCategory(UUID id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.batches = new ArrayList<>();
    }

    public void addBatch(TicketBatch batch) {
        if (batch == null) {
            throw new IllegalArgumentException("Batch cannot be null.");
        }
        boolean hasDuplicateNumber = this.batches.stream()
                .anyMatch(b -> b.getBatchNumber().equals(batch.getBatchNumber()));
        if (hasDuplicateNumber) {
            throw new RuntimeException("A batch with number " + batch.getBatchNumber() + " already exists in this category.");
        }
        this.batches.add(batch);
    }
}
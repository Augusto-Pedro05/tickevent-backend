package com.tickevent.app.domain.models;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class TicketBatch {

    public enum BatchStatus {
        DRAFT,
        ACTIVE,
        PAUSED,
        SOLD_OUT,
        FINISHED
    }

    @Setter(AccessLevel.NONE)
    @NotNull(message = "Batch ID is required.")
    private UUID id;

    @NotBlank(message = "Batch name is required.")
    private String name;

    @NotNull(message = "Batch number is required.")
    @Min(value = 1, message = "Batch number must be at least 1.")
    private Integer batchNumber;

    @NotNull(message = "Price is required.")
    @DecimalMin(value = "0.0", message = "Price cannot be negative.")
    private BigDecimal price;

    @NotNull(message = "Total limit is required.")
    @Min(value = 1, message = "Total limit must be greater than zero.")
    private Integer totalLimit;

    @NotNull(message = "Available quantity is required.")
    @Min(value = 0, message = "Available quantity cannot be negative.")
    private Integer availableQuantity;

    @NotNull(message = "Sales start date is required.")
    private LocalDateTime salesStartDate;

    @NotNull(message = "Sales end date is required.")
    private LocalDateTime salesEndDate;

    @Setter(AccessLevel.NONE)
    @NotNull(message = "Batch status is required.")
    private BatchStatus status;

    public TicketBatch(UUID id,
                       String name,
                       Integer batchNumber,
                       BigDecimal price,
                       Integer totalLimit,
                       Integer availableQuantity,
                       LocalDateTime salesStartDate,
                       LocalDateTime salesEndDate) {
        this.id = id;
        this.name = name;
        this.batchNumber = batchNumber;
        this.price = price;
        this.totalLimit = totalLimit;
        this.availableQuantity = availableQuantity;
        this.salesStartDate = salesStartDate;
        this.salesEndDate = salesEndDate;
        this.status = BatchStatus.DRAFT;
    }

    public void activate() {
        validate();
        if (this.availableQuantity == 0) {
            throw new RuntimeException("Cannot activate a batch with zero available tickets.");
        }
        this.status = BatchStatus.ACTIVE;
    }

    public void pause() {
        if (this.status != BatchStatus.ACTIVE) {
            throw new RuntimeException("Only active batches can be paused.");
        }
        this.status = BatchStatus.PAUSED;
    }

    public void markAsSoldOut() {
        this.availableQuantity = 0;
        this.status = BatchStatus.SOLD_OUT;
    }

    public void validate() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<TicketBatch>> violations = validator.validate(this);

            if (!violations.isEmpty()) {
                String errorMessage = violations.iterator().next().getMessage();
                throw new RuntimeException(errorMessage);
            }
        }

        if (this.salesStartDate != null && this.salesEndDate != null && this.salesEndDate.isBefore(this.salesStartDate)) {
            throw new RuntimeException("Sales end date must be after start date.");
        }

        if (this.availableQuantity != null && this.totalLimit != null && this.availableQuantity > this.totalLimit) {
            throw new RuntimeException("Available quantity cannot exceed the total limit.");
        }
    }
}
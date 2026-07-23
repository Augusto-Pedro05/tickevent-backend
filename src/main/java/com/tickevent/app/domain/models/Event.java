package com.tickevent.app.domain.models;

import com.tickevent.app.domain.models.makers.PublishingGroup;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class Event {

    public enum Status {
        DRAFT,
        PUBLISHED,
        CANCELED,
        FINISHED,
        SUSPENDED
    }
    @Setter(AccessLevel.NONE)
    @NotNull(message = "Event ID is required.")
    private UUID id;

    @NotBlank(message = "Title is required.")
    private String title;

    @NotBlank(message = "Description is required.", groups = PublishingGroup.class)
    private String description;

    @NotBlank(message = "Banner URL is required.", groups = PublishingGroup.class)
    private String bannerUrl;

    @NotBlank(message = "Category is required.", groups = PublishingGroup.class)
    private String category;

    @NotNull(message = "Max capacity is required.", groups = PublishingGroup.class)
    @Min(value = 1, message = "Max capacity must be greater than zero.", groups = PublishingGroup.class)
    private Integer maxCapacity;

    @NotNull(message = "Location is required.")
    private Location location;

    @NotNull(message = "Max tickets per user is required.", groups = PublishingGroup.class)
    @Min(value = 1, message = "Max tickets per user must be greater than zero.", groups = PublishingGroup.class)
    private Integer maxTicketsPerUser;

    @NotNull(message = "Start date is required.")
    private LocalDateTime startDateTime;

    @NotNull(message = "End date is required.")
    private LocalDateTime endDateTime;

    @Setter(AccessLevel.NONE)
    @NotNull(message = "Event status is required.")
    private Status status;

    @NotNull(message = "Event creator is required.")
    private User creator;

    @NotEmpty(message = "At least one ticket category is required.", groups = PublishingGroup.class)
    private List<TicketCategory> ticketCategories;

    public Event(UUID id,
                 String title,
                 String description,
                 LocalDateTime startDateTime,
                 LocalDateTime endDateTime,
                 Location location,
                 User creator) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.location = location;
        this.creator = creator;
        this.status = Status.DRAFT;
    }

    public void deactivate() {
        this.status = Status.CANCELED;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        if (maxCapacity > 0) {
            this.maxCapacity = maxCapacity;
        }
    }

    public void setMaxTicketsPerUser(Integer maxTicketsPerUser) {
        if (maxTicketsPerUser > 0) {
            this.maxTicketsPerUser = maxTicketsPerUser;
        }
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        if (this.startDateTime != null && endDateTime.isBefore(this.startDateTime)) {
            throw new RuntimeException("End date cannot be earlier than start date.");
        }
        this.endDateTime = endDateTime;
    }

    public void publish() {
        validateForPublishing();
        this.status = Status.PUBLISHED;
    }

    private void validateForPublishing() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Event>> violations = validator.validate(this, PublishingGroup.class);

            if (!violations.isEmpty()) {
                String errorMessage = violations.iterator().next().getMessage();
                throw new RuntimeException(errorMessage);
            }
        }

        if (this.startDateTime != null && this.endDateTime != null && this.endDateTime.isBefore(this.startDateTime)) {
            throw new RuntimeException("End date must be after start date.");
        }

        if (this.status != Status.DRAFT) {
            throw new RuntimeException("Only draft events can be published.");
        }
    }
}
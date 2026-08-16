package com.tickevent.app.utils;

import com.tickevent.app.adapters.outbound.entities.EventEntity;
import com.tickevent.app.adapters.outbound.entities.TicketBatchEntity;
import com.tickevent.app.adapters.outbound.entities.TicketCategoryEntity;
import com.tickevent.app.adapters.outbound.entities.UserEntity;
import com.tickevent.app.domain.models.Event;
import com.tickevent.app.domain.models.TicketBatch;
import com.tickevent.app.domain.models.TicketCategory;
import com.tickevent.app.domain.models.User;
import com.tickevent.app.utils.mappers.EventMapper;
import com.tickevent.app.utils.mappers.TicketBatchMapper;
import com.tickevent.app.utils.mappers.TicketCategoryMapper;
import com.tickevent.app.utils.mappers.UserMapper;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Utilitário inteligente para preparação de cenários de teste.
 * Garante a criação em cascata das dependências de banco de dados.
 */
@TestComponent
public class TestDataBuilder {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private TicketCategoryMapper categoryMapper;

    @Autowired
    private TicketBatchMapper batchMapper;

    public User persistUser() {
        User creator = new User(
                UUID.randomUUID(),
                "User",
                "user@email.com",
                "pass123",
                "1111111111",
                LocalDateTime.now(),
                "12345678900",
                LocalDate.of(2000, 1, 1)
        );
        UserEntity creatorEntity = userMapper.toEntity(creator);
        entityManager.persist(creatorEntity);
        return creator;
    }

    public Event persistEvent() {
        return persistEvent(persistUser());
    }

    public Event persistEvent(User creator) {
        Event event = new Event(
                UUID.randomUUID(),
                "Event Title",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                creator
        );

        EventEntity eventEntity = eventMapper.toEntity(event);
        UserEntity creatorEntity = entityManager.getReference(UserEntity.class, creator.getId());
        eventEntity.setCreator(creatorEntity);
        entityManager.persist(eventEntity);
        return event;
    }
    public TicketCategory persistCategory() {
        return persistCategory(persistEvent());
    }
    public TicketCategory persistCategory(Event event) {
        TicketCategory category = new TicketCategory(
                UUID.randomUUID(),
                "Category Name",
                "Category Description"
        );

        TicketCategoryEntity categoryEntity = categoryMapper.toEntity(category);
        EventEntity eventEntity = entityManager.getReference(EventEntity.class, event.getId());
        categoryEntity.setEvent(eventEntity);
        entityManager.persist(categoryEntity);
        return category;
    }
    public TicketBatch persistBatch() {
        return persistBatch(persistCategory());
    }
    public TicketBatch persistBatch(TicketCategory category) {

        TicketBatch batch = new TicketBatch(
                UUID.randomUUID(),
                "Batch Name",
                1,
                new BigDecimal("100.00"),
                100,
                100,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1)
        );

        TicketBatchEntity batchEntity = batchMapper.toEntity(batch);
        TicketCategoryEntity categoryEntity = entityManager.getReference(TicketCategoryEntity.class, category.getId());
        batchEntity.setCategory(categoryEntity);
        entityManager.persist(batchEntity);

        return batch;
    }
}

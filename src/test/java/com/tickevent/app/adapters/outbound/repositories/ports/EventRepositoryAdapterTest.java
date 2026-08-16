package com.tickevent.app.adapters.outbound.repositories.ports;

import com.tickevent.app.adapters.outbound.ports.EventRepositoryAdapter;
import com.tickevent.app.domain.models.Event;
import com.tickevent.app.domain.models.User;
import com.tickevent.app.utils.TestDataBuilder;
import com.tickevent.app.utils.mappers.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de integração para o Adapter de Repositório de Eventos.
 * Valida a persistência real no banco de dados H2 e o mapeamento correto
 * entre os Modelos de Domínio e as Entidades JPA.
 */
@DataJpaTest
@Import({
        EventRepositoryAdapter.class,
        EventMapperImpl.class,
        TicketCategoryMapperImpl.class,
        TicketBatchMapperImpl.class,
        UserMapperImpl.class,
        TestDataBuilder.class
})
class EventRepositoryAdapterTest {

    @Autowired
    private EventRepositoryAdapter eventRepositoryAdapter;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TestDataBuilder testDataBuilder;

    @Test
    @DisplayName("Should save an event to the database and find it by ID")
    void shouldSaveAndFindEventSuccessfully() {
        // --- ARRANGE ---
        User creator = testDataBuilder.persistUser();
        Event event = new Event(
                UUID.randomUUID(),
                "Event Title",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                creator
        );

        // --- ACT ---
        Event savedEvent = eventRepositoryAdapter.save(event);
        entityManager.flush();
        entityManager.clear();
        Optional<Event> foundEvent = eventRepositoryAdapter.findById(savedEvent.getId());

        // --- ASSERT ---
        assertTrue(foundEvent.isPresent(), "The event should be found in the database");
        assertEquals(event.getTitle(), foundEvent.get().getTitle());
        assertEquals(event.getCreator().getEmail(), foundEvent.get().getCreator().getEmail());
    }
}
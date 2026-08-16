package com.tickevent.app.adapters.outbound.repositories.ports;

import com.tickevent.app.adapters.outbound.entities.UserEntity;
import com.tickevent.app.adapters.outbound.ports.EventRepositoryAdapter;
import com.tickevent.app.domain.models.Event;
import com.tickevent.app.domain.models.Location;
import com.tickevent.app.domain.models.User;
import com.tickevent.app.utils.mappers.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({
        EventRepositoryAdapter.class,
        EventMapperImpl.class,
        TicketCategoryMapperImpl.class,
        TicketBatchMapperImpl.class,
        UserMapperImpl.class
})
class EventRepositoryAdapterTest {

    @Autowired
    private EventRepositoryAdapter eventRepositoryAdapter;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserMapper userMapper;

    @Test
    @DisplayName("Deve salvar um evento no banco de dados e encontrá-lo pelo ID")
    void shouldSaveAndFindEventSuccessfully() {
        // --- ARRANGE ---
        User creatorDomain = new User(
                UUID.randomUUID(), "Pedro", "pedro@email.com", "senha123",
                "1199999999", LocalDateTime.now(), "12345678900", LocalDate.of(2005, 5, 2)
        );

        UserEntity creatorEntity = userMapper.toEntity(creatorDomain);

        entityManager.persist(creatorEntity);

        Event newEvent = new Event(
                UUID.randomUUID(),
                "Tech Summit",
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(12),
                creatorDomain
        );

        // --- ACT ---
        Event savedEvent = eventRepositoryAdapter.save(newEvent);
        entityManager.flush();
        entityManager.clear();

        Optional<Event> foundEvent = eventRepositoryAdapter.findById(savedEvent.getId());

        // --- ASSERT ---
        assertTrue(foundEvent.isPresent(), "O evento deveria ser encontrado no banco de dados");
        assertEquals("Tech Summit", foundEvent.get().getTitle());
        assertEquals("pedro@email.com", foundEvent.get().getCreator().getEmail());
    }
}
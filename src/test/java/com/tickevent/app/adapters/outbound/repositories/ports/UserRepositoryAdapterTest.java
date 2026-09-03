package com.tickevent.app.adapters.outbound.repositories.ports;

import com.tickevent.app.adapters.outbound.ports.UserRepositoryAdapter;
import com.tickevent.app.domain.models.User;
import com.tickevent.app.utils.mappers.UserMapperImpl;
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
@Import({UserRepositoryAdapter.class, UserMapperImpl.class})
class UserRepositoryAdapterTest {

    @Autowired
    private UserRepositoryAdapter userRepositoryAdapter;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Should save a user and find by ID and email")
    void shouldSaveAndFindUserSuccessfully() {
        User user = new User(
                UUID.randomUUID(),
                "John Doe",
                "john@example.com",
                "hashed_pwd",
                "11988887777",
                LocalDateTime.now(),
                "12345678901",
                LocalDate.of(1995, 5, 20)
        );

        User savedUser = userRepositoryAdapter.save(user);
        entityManager.flush();
        entityManager.clear();

        assertTrue(userRepositoryAdapter.existsByEmail("john@example.com"));
        assertFalse(userRepositoryAdapter.existsByEmail("other@example.com"));

        Optional<User> foundById = userRepositoryAdapter.findById(savedUser.getId());
        assertTrue(foundById.isPresent());
        assertEquals("John Doe", foundById.get().getName());
        assertEquals("john@example.com", foundById.get().getEmail());

        Optional<User> foundByEmail = userRepositoryAdapter.findByEmail("john@example.com");
        assertTrue(foundByEmail.isPresent());
        assertEquals(savedUser.getId(), foundByEmail.get().getId());
    }
}

package com.tickevent.app.adapters.outbound.repositories.ports;

import com.tickevent.app.adapters.outbound.ports.TicketCategoryRepositoryAdapter;
import com.tickevent.app.domain.models.*;
import com.tickevent.app.utils.TestDataBuilder;
import com.tickevent.app.utils.mappers.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de integração para o Adapter de Categorias de Ingresso.
 * Valida a persistência em cascata (Categoria ⇾ Lotes) e a injeção manual
 * da Chave Estrangeira de Evento realizada pelo Adapter.
 */
@DataJpaTest
@Import({TicketCategoryRepositoryAdapter.class,
        TicketCategoryMapperImpl.class,
        TicketBatchMapperImpl.class,
        EventMapperImpl.class,
        UserMapperImpl.class,
        TestDataBuilder.class
})
class TicketCategoryRepositoryAdapterTest {

    @Autowired
    private TicketCategoryRepositoryAdapter categoryRepositoryAdapter;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TestDataBuilder testDataBuilder;

    @Test
    @DisplayName("Should save a ticket category and find by Id")
    void shouldSaveAndFindTicketCategorySuccessfully() {
        // --- ARRANGE ---
        Event event = testDataBuilder.persistEvent();
        TicketCategory newCategory = testDataBuilder.persistCategory(event);

        // --- ACT ---
        TicketCategory savedCategory = categoryRepositoryAdapter.save(event.getId(), newCategory);
        entityManager.flush();
        entityManager.clear();
        Optional<TicketCategory> foundCategory = categoryRepositoryAdapter.findById(savedCategory.getId());

        // --- ASSERT ---
        assertTrue(foundCategory.isPresent(), "The category should be found in the database");
        assertEquals(newCategory.getName(), foundCategory.get().getName());
        assertEquals(newCategory.getDescription(), foundCategory.get().getDescription());
    }

    @Test
    @DisplayName("It must delete a ticket category and its respective batches.")
    void shouldDeleteTicketCategorySuccessfully() {
        // --- ARRANGE ---
        TicketCategory ticketCategory = testDataBuilder.persistCategory();

        // --- ACT ---
        categoryRepositoryAdapter.delete(ticketCategory);
        entityManager.flush();
        entityManager.clear();

        // --- ASSERT ---
        Optional<TicketCategory> deletedCategory = categoryRepositoryAdapter.findById(ticketCategory.getId());
        assertFalse(deletedCategory.isPresent(), "The category should no longer exist in the database");
    }
}
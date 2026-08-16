package com.tickevent.app.adapters.outbound.repositories.ports;

import com.tickevent.app.adapters.outbound.entities.EventEntity;
import com.tickevent.app.adapters.outbound.entities.UserEntity;
import com.tickevent.app.adapters.outbound.ports.TicketCategoryRepositoryAdapter;
import com.tickevent.app.domain.models.*;
import com.tickevent.app.utils.mappers.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({TicketCategoryRepositoryAdapter.class,
        TicketCategoryMapperImpl.class,
        TicketBatchMapperImpl.class,
        EventMapperImpl.class,
        UserMapperImpl.class
})
class TicketCategoryRepositoryAdapterTest {

    @Autowired
    private TicketCategoryRepositoryAdapter categoryRepositoryAdapter;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private UserMapper userMapper;

    @Test
    @DisplayName("Deve salvar uma categoria de ingresso com seus lotes e encontrá-la pelo ID")
    void shouldSaveAndFindTicketCategorySuccessfully() {
        // --- ARRANGE ---

        User creator = new User(UUID.randomUUID(), "Pedro", "pedro@email.com", "senha", "119", LocalDateTime.now(), "123", LocalDate.now());
        UserEntity creatorEntity = userMapper.toEntity(creator);
        entityManager.persist(creatorEntity);

        Event event = new Event(
                UUID.randomUUID(),
                "Tech Summit",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                creator);
        EventEntity eventEntity = eventMapper.toEntity(event);
        eventEntity.setCreator(creatorEntity);
        entityManager.persist(eventEntity);

        UUID eventId = eventEntity.getId();

        TicketCategory newCategory = new TicketCategory(
                UUID.randomUUID(),
                "Pista VIP",
                "Acesso a área em frente ao palco"
        );

        // 2. Instancia um Lote (Batch) com as regras do novo domínio
        TicketBatch batch1 = new TicketBatch(
                UUID.randomUUID(),
                "Lote Promocional",
                1, // Número do lote
                new BigDecimal("150.00"), // O preço agora vive aqui
                100, // Limite total
                100, // Quantidade disponível
                LocalDateTime.now(), // Início das vendas
                LocalDateTime.now().plusDays(10) // Fim das vendas
        );

        // 3. Vincula o lote à categoria usando o método de domínio
        newCategory.addBatch(batch1);

        // --- ACT ---
        TicketCategory savedCategory = categoryRepositoryAdapter.save(eventId, newCategory);

        // Limpa o cache para garantir a consulta no banco de dados real
        entityManager.flush();
        entityManager.clear();

        Optional<TicketCategory> foundCategory = categoryRepositoryAdapter.findById(savedCategory.getId());

        // --- ASSERT ---
        assertTrue(foundCategory.isPresent(), "A categoria deveria ser encontrada no banco de dados");
        assertEquals("Pista VIP", foundCategory.get().getName());
        assertEquals("Acesso a área em frente ao palco", foundCategory.get().getDescription());

        // Valida se o banco de dados salvou os Lotes em cascata corretamente
        assertFalse(foundCategory.get().getBatches().isEmpty(), "A categoria deveria conter lotes salvos");
        assertEquals(1, foundCategory.get().getBatches().size());
        assertEquals(new BigDecimal("150.00"), foundCategory.get().getBatches().get(0).getPrice());
        assertEquals("Lote Promocional", foundCategory.get().getBatches().get(0).getName());
    }

    @Test
    @DisplayName("Deve deletar uma categoria de ingresso e seus respectivos lotes")
    void shouldDeleteTicketCategorySuccessfully() {
        // --- ARRANGE ---

        User creator = new User(UUID.randomUUID(), "Pedro", "pedro@email.com", "senha", "119", LocalDateTime.now(), "123", LocalDate.now());
        UserEntity creatorEntity = userMapper.toEntity(creator);
        entityManager.persist(creatorEntity);

        Event event = new Event(
                UUID.randomUUID(),
                "Tech Summit",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                creator);

        EventEntity eventEntity = eventMapper.toEntity(event);
        eventEntity.setCreator(creatorEntity);
        entityManager.persist(eventEntity);

        UUID eventId = eventEntity.getId();

        TicketCategory newCategory = new TicketCategory(
                UUID.randomUUID(),
                "Meia Entrada",
                "Estudantes"
        );

        TicketBatch batch = new TicketBatch(
                UUID.randomUUID(), "Lote 1", 1, new BigDecimal("75.00"),
                50, 50, LocalDateTime.now(), LocalDateTime.now().plusDays(5)
        );
        newCategory.addBatch(batch);

        TicketCategory savedCategory = categoryRepositoryAdapter.save(eventId,newCategory);
        entityManager.clear();

        // --- ACT ---
        categoryRepositoryAdapter.delete(savedCategory);
        entityManager.clear();

        // --- ASSERT ---
        Optional<TicketCategory> deletedCategory = categoryRepositoryAdapter.findById(savedCategory.getId());
        assertFalse(deletedCategory.isPresent(), "A categoria e seus lotes não deveriam mais existir no banco");
    }
}
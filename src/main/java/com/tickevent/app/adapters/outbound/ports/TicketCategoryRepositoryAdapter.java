package com.tickevent.app.adapters.outbound.ports;

import com.tickevent.app.adapters.outbound.entities.EventEntity;
import com.tickevent.app.adapters.outbound.repositories.SpringDataTicketCategoryRepository;
import com.tickevent.app.application.ports.out.TicketCategoryRepository;
import com.tickevent.app.domain.models.TicketCategory;
import com.tickevent.app.utils.mappers.TicketCategoryMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Component
@RequiredArgsConstructor
public class TicketCategoryRepositoryAdapter implements TicketCategoryRepository {

    private final SpringDataTicketCategoryRepository springRepository;
    private final TicketCategoryMapper categoryMapper;
    private final EntityManager entityManager;

    @Override
    public TicketCategory save(UUID eventId, TicketCategory category) {
        var entity = categoryMapper.toEntity(category);
        var eventReference = entityManager.getReference(EventEntity.class, eventId);
        entity.setEvent(eventReference);
        var savedEntity = springRepository.save(entity);
        return categoryMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<TicketCategory> findById(UUID id) {
        return springRepository.findById(id)
                .map(categoryMapper::toDomain);
    }

    @Override
    public void delete(TicketCategory category) {
        springRepository.deleteById(category.getId());
    }
}

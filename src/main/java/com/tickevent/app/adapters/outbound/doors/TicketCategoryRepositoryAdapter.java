package com.tickevent.app.adapters.outbound.doors;

import com.tickevent.app.adapters.outbound.repositories.SpringDataTicketCategoryRepository;
import com.tickevent.app.application.ports.out.TicketCategoryRepository;
import com.tickevent.app.domain.models.TicketCategory;
import com.tickevent.app.utils.mappers.TicketCategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TicketCategoryRepositoryAdapter implements TicketCategoryRepository {

    private final SpringDataTicketCategoryRepository springRepository;
    private final TicketCategoryMapper categoryMapper;

    @Override
    public TicketCategory save(TicketCategory category) {
        var entity = categoryMapper.toEntity(category);
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

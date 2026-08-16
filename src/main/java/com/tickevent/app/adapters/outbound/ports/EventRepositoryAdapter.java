package com.tickevent.app.adapters.outbound.ports;

import com.tickevent.app.adapters.outbound.repositories.SpringDataEventRepository;
import com.tickevent.app.application.ports.out.EventRepository;
import com.tickevent.app.domain.models.Event;
import com.tickevent.app.utils.mappers.EventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Component
@RequiredArgsConstructor
public class EventRepositoryAdapter implements EventRepository {

    private final SpringDataEventRepository springRepository;
    private final EventMapper eventMapper;

    @Override
    public Event save(Event event) {
        var entity = eventMapper.toEntity(event);
        var savedEntity = springRepository.save(entity);
        return eventMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Event> findById(UUID id) {
        return springRepository.findById(id)
                .map(eventMapper::toDomain);
    }
}
package com.tickevent.app.utils.mappers;

import com.tickevent.app.adapters.outbound.entities.EventEntity;
import com.tickevent.app.adapters.outbound.entities.embeddables.LocationEmbeddable;
import com.tickevent.app.domain.models.Event;
import com.tickevent.app.domain.models.Location;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(uses = {TicketCategoryMapper.class, UserMapper.class})
public interface EventMapper {

    EventEntity toEntity(Event domain);

    Event toDomain(EventEntity entity);

    // Criando esses métodos, o MapStruct sabe como converter o atributo interno 'location'
    LocationEmbeddable locationToEmbeddable(Location location);
    Location embeddableToLocation(LocationEmbeddable embeddable);

    @AfterMapping
    default void linkCategories(@MappingTarget EventEntity entity) {
        if (entity.getTicketCategories() != null) {
            entity.getTicketCategories().forEach(category -> category.setEvent(entity));
        }
    }
}
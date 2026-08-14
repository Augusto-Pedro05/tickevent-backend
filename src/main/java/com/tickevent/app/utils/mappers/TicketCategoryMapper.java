package com.tickevent.app.utils.mappers;

import com.tickevent.app.adapters.outbound.entities.TicketCategoryEntity;
import com.tickevent.app.domain.models.TicketCategory;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(uses = {TicketBatchMapper.class})
public interface TicketCategoryMapper {

    @Mapping(target = "event", ignore = true)
    TicketCategoryEntity toEntity(TicketCategory domain);

    TicketCategory toDomain(TicketCategoryEntity entity);

    @AfterMapping
    default void linkBatches(@MappingTarget TicketCategoryEntity entity) {
        if (entity.getBatches() != null) {
            entity.getBatches().forEach(batch -> batch.setCategory(entity));
        }
    }
}
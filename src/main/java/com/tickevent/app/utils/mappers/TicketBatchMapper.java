package com.tickevent.app.utils.mappers;

import com.tickevent.app.adapters.outbound.entities.TicketBatchEntity;
import com.tickevent.app.domain.models.TicketBatch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TicketBatchMapper {

    @Mapping(target = "category", ignore = true)
    TicketBatchEntity toEntity(TicketBatch domain);
    TicketBatch toDomain(TicketBatchEntity entity);
}
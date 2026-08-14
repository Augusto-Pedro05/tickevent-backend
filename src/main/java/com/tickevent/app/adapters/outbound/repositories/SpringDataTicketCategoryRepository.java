package com.tickevent.app.adapters.outbound.repositories;

import com.tickevent.app.adapters.outbound.entities.TicketCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataTicketCategoryRepository extends JpaRepository<TicketCategoryEntity, UUID> {
}

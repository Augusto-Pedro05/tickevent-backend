package com.tickevent.app.application.service.catalog;

import com.tickevent.app.application.ports.out.EventRepository;
import com.tickevent.app.application.ports.out.UserRepository;
import com.tickevent.app.domain.dtos.EventCreationDTO;
import com.tickevent.app.domain.dtos.EventUpdateDTO;
import com.tickevent.app.domain.models.Event;
import com.tickevent.app.domain.models.Location;
import com.tickevent.app.domain.models.User;

import java.util.UUID;

import static com.tickevent.app.application.service.utils.PatchUtils.updateIfPresent;
import static com.tickevent.app.application.service.utils.PatchUtils.isLocationIncomplete;

public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public Event createEvent(UUID requesterId, EventCreationDTO dto) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if(requester.getRole() != User.Role.ADMIN){
            throw new RuntimeException("Unauthorized access");
        }

        Location eventLocation = new Location(
                dto.location().venueName(),
                dto.location().street(),
                dto.location().number(),
                dto.location().city(),
                dto.location().state()
        );

        Event newEvent = new Event(
                UUID.randomUUID(),
                dto.title(),
                dto.description(),
                dto.startDate(),
                dto.endDate(),
                eventLocation,
                requester
        );

        return eventRepository.save(newEvent);
    }

    public Event deactivateEvent(UUID eventId, UUID requesterId){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(requester.getRole() != User.Role.ADMIN){
            throw new RuntimeException("Access denied: only users with admin role perform this action");
        }else if(!event.getCreatorId().equals(requesterId)){
            throw new RuntimeException("Access denied: the requester is not owner");
        }
        event.deactivate();

        return eventRepository.save(event);
    }

    public Event updateEvent(UUID eventId, UUID requesterId, EventUpdateDTO dto) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(requester.getRole() != User.Role.ADMIN){
            throw new RuntimeException("Access denied: only users with admin role perform this action");
        } else if(!event.getCreatorId().equals(requesterId)){
            throw new RuntimeException("Access denied: the requester is not owner");
        }

        updateIfPresent(dto.title(), event::setTitle);
        updateIfPresent(dto.description(), event::setDescription);
        updateIfPresent(dto.bannerUrl(), event::setBannerUrl);
        updateIfPresent(dto.category(), event::setCategory);
        updateIfPresent(dto.maxCapacity(), event::setMaxCapacity);
        updateIfPresent(dto.maxTicketsPerUser(), event::setMaxTicketsPerUser);
        updateIfPresent(dto.startDateTime(), event::setStartDateTime);
        updateIfPresent(dto.endDateTime(), event::setEndDateTime);

        if (dto.location() != null) {
            Location currentLocation = event.getLocation();

            if (currentLocation != null) {
                updateIfPresent(dto.location().venueName(), currentLocation::setVenueName);
                updateIfPresent(dto.location().street(), currentLocation::setStreet);
                updateIfPresent(dto.location().number(), currentLocation::setNumber);
                updateIfPresent(dto.location().city(), currentLocation::setCity);
                updateIfPresent(dto.location().state(), currentLocation::setState);
            } else {
                if (isLocationIncomplete(dto.location())) {
                    throw new RuntimeException("All the fields may contain a valid value when the location is null");
                }
                Location newLocation = new Location(
                        dto.location().venueName(),
                        dto.location().street(),
                        dto.location().number(),
                        dto.location().city(),
                        dto.location().state()
                );
                event.setLocation(newLocation);
            }
        }
        return eventRepository.save(event);
    }

    public Event postEvent(UUID eventId, UUID requesterId){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(requester.getRole() != User.Role.ADMIN){
            throw new RuntimeException("Access denied: only users with admin role perform this action");
        } else if(!event.getCreatorId().equals(requesterId)){
            throw new RuntimeException("Access denied: the requester is not owner");
        }

        event.publish();
        return eventRepository.save(event);
    }
}

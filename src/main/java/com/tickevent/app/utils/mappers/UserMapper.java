package com.tickevent.app.utils.mappers;

import com.tickevent.app.adapters.outbound.entities.UserEntity;
import com.tickevent.app.domain.models.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    UserEntity toEntity(User domain);

    default User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        if (User.Role.ADMIN.name().equals(entity.getRole())) {
            return new User(
                    entity.getId(),
                    entity.getName(),
                    entity.getEmail(),
                    entity.getPassword(),
                    entity.getPhoneNumber(),
                    entity.getCreatedAt(),
                    entity.getDocument(),
                    entity.getCommercialName(),
                    entity.getBankAccountDetails(),
                    entity.getIsApproved()
            );
        }
        return new User(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getPhoneNumber(),
                entity.getCreatedAt(),
                entity.getDocument(),
                entity.getBirthDate()
        );
    }
}
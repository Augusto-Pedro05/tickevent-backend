package com.tickevent.app.adapters.outbound.ports;

import com.tickevent.app.adapters.outbound.repositories.SpringDataUserRepository;
import com.tickevent.app.application.ports.out.UserRepository;
import com.tickevent.app.domain.models.User;
import com.tickevent.app.utils.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final SpringDataUserRepository springRepository;
    private final UserMapper userMapper;

    @Override
    public boolean existsByEmail(String email) {
        return springRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        var entity = userMapper.toEntity(user);
        var savedEntity = springRepository.save(entity);
        return userMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springRepository.findByEmail(email)
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return springRepository.findById(id)
                .map(userMapper::toDomain);
    }
}

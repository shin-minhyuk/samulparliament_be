package com.samulparliament_be.domain.users.repository;

import com.samulparliament_be.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByDeletedAtIsNull();
    Optional<User> findByIdAndDeletedAtIsNull(Long id);
}

package com.samulparliament_be.domain.users.service;

import com.samulparliament_be.domain.users.entity.User;
import com.samulparliament_be.domain.users.repository.UserRepository;
import com.samulparliament_be.global.exception.BusinessException;
import com.samulparliament_be.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public User getById(Long id) {
        return userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
}

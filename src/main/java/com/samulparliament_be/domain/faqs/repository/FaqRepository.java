package com.samulparliament_be.domain.faqs.repository;

import com.samulparliament_be.domain.faqs.entity.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FaqRepository extends JpaRepository<Faq, Long> {

    List<Faq> findByDeletedAtIsNull();
    Optional<Faq> findByIdAndDeletedAtIsNull(Long id);
}

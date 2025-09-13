package com.tannguyen.ai.repository.primary;

import com.tannguyen.ai.model.primary.PageAccess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PageAccessRepository extends JpaRepository<PageAccess, Long> {
    Optional<PageAccess> findByUrl(String url);
}
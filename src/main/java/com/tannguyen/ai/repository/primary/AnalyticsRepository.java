package com.tannguyen.ai.repository.primary;

import com.tannguyen.ai.model.primary.Analytics;
import com.tannguyen.ai.model.primary.Role;
import com.tannguyen.ai.model.primary.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {
    List<Analytics> findAllByUsersContainingOrRolesIn(User user, Collection<Role> roles);

    Page<Analytics> findAllByUsersContainingOrRolesIn(User user, Set<Role> roles, Pageable pageable);
}

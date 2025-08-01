package com.tannguyen.ai.repository;

import com.tannguyen.ai.model.Analytics;
import com.tannguyen.ai.model.Role;
import com.tannguyen.ai.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {
    List<Analytics> findAllByUsersContainingAndRolesIn(User user, Collection<Role> roles);
}
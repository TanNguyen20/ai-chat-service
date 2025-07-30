package com.tannguyen.ai.repository;

import com.tannguyen.ai.model.Analytics;
import com.tannguyen.ai.model.Role;
import com.tannguyen.ai.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {
    List<Analytics> findAllByUsersInOrRolesIn(Collection<List<User>> users, Collection<Set<Role>> roles);
}

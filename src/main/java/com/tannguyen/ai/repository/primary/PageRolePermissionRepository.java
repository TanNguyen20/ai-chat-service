package com.tannguyen.ai.repository.primary;

import com.tannguyen.ai.model.embeddable.PageRolePermissionId;
import com.tannguyen.ai.model.primary.PageAccess;
import com.tannguyen.ai.model.primary.PageRolePermission;
import com.tannguyen.ai.model.primary.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PageRolePermissionRepository extends JpaRepository<PageRolePermission, PageRolePermissionId> {
    List<PageRolePermission> findByPage(PageAccess page);

    List<PageRolePermission> findByRole(Role role);
}
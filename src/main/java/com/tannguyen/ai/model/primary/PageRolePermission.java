package com.tannguyen.ai.model.primary;

import com.tannguyen.ai.model.embeddable.PageRolePermissionId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "page_role_permissions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"page_id", "role_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageRolePermission {

    @EmbeddedId
    private PageRolePermissionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pageId")
    @JoinColumn(name = "page_id")
    private PageAccess page;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(nullable = false)
    private boolean canCreate;
    @Column(nullable = false)
    private boolean canRead;
    @Column(nullable = false)
    private boolean canUpdate;
    @Column(nullable = false)
    private boolean canDelete;
}
package com.tannguyen.ai.model.embeddable;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PageRolePermissionId implements Serializable {
    private Long pageId;
    private Long roleId;
}

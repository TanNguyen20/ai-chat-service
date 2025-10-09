package com.tannguyen.ai.model.primary;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pages", uniqueConstraints = @UniqueConstraint(columnNames = "url"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageAccess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 256, unique = true)
    private String url;

    @Column(nullable = false, length = 512)
    private String description;

    @Column(nullable = false, length = 512)
    private String name;

    @Column(nullable = false, length = 512)
    private String icon;
}

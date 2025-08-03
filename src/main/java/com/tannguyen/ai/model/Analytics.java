package com.tannguyen.ai.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "analytics")
public class Analytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dashboard_id")
    private String dashboardId;

    @Column(name = "dashboard_title")
    private String dashboardTitle;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "analytics_roles",
            joinColumns = @JoinColumn(name = "analytics_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "analytics_user",
            joinColumns = @JoinColumn(name = "analytics_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analytics_config_id", nullable = false)
    private AnalyticsConfig analyticsConfig;
}

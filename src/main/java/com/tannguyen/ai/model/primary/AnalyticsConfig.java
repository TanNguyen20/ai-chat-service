package com.tannguyen.ai.model.primary;

import com.tannguyen.ai.model.audit.Auditable;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "analytics_config")
public class AnalyticsConfig extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hostname")
    private String hostname;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

}

package com.tannguyen.ai.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "chatbot_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotInfo {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "uuid", updatable = false, nullable = false)
    private String uuid;

    @Column(name = "name")
    private String name;

    @Column(name = "allowed_host")
    private String allowedHost;

    @Column(name = "theme_color")
    private String themeColor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

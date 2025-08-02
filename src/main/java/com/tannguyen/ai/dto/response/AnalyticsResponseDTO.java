package com.tannguyen.ai.dto.response;

import com.tannguyen.ai.enums.RoleName;
import com.tannguyen.ai.model.Analytics;
import com.tannguyen.ai.model.Role;
import com.tannguyen.ai.model.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
public class AnalyticsResponseDTO {
    private Long id;

    private String dashboardId;

    private String dashboardHost;

    private String dashboardTitle;

    private Set<RoleName> roles;

    private List<String> users;

    public static AnalyticsResponseDTO from(Analytics analytics) {
        return AnalyticsResponseDTO.builder()
                .id(analytics.getId())
                .dashboardId(analytics.getDashboardId())
                .dashboardHost(analytics.getDashboardHost())
                .dashboardTitle(analytics.getDashboardTitle())
                .roles(analytics.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                .users(analytics.getUsers().stream().map(User::getUsername).toList())
                .build();
    }
}
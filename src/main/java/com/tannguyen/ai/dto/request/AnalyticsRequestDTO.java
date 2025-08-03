package com.tannguyen.ai.dto.request;

import com.tannguyen.ai.enums.RoleName;
import lombok.Data;

import java.util.List;

@Data
public class AnalyticsRequestDTO {
    private String dashboardId;

    private String dashboardTitle;

    private List<String> users;

    private List<RoleName> roles;

    private Long analyticsConfigId;
}

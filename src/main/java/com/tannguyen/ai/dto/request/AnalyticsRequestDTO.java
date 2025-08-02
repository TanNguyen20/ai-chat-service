package com.tannguyen.ai.dto.request;

import com.tannguyen.ai.enums.RoleName;
import lombok.Data;

import java.util.List;

@Data
public class AnalyticsRequestDTO {
    String dashboardId;

    String dashboardHost;

    String dashboardTitle;

    List<String> users;

    List<RoleName> roles;
}

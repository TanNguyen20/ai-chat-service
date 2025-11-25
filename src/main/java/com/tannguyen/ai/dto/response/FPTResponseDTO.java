package com.tannguyen.ai.dto.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class FPTResponseDTO {
    private UUID id;
    private String odataType;
    private String displayName;
    private String givenName;
    private String surname;
    private String jobTitle;
    private String mail;
    private String mobilePhone;
    private String userPrincipalName;
    private String officeLocation;
    private String preferredLanguage;
    private List<String> businessPhones;
}

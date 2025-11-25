package com.tannguyen.ai.model.third.base;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class FPTBase {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "odata_type")
    private String odataType;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "given_name")
    private String givenName;

    @Column(name = "surname")
    private String surname;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "mail")
    private String mail;

    @Column(name = "mobile_phone")
    private String mobilePhone;

    @Column(name = "user_principal_name")
    private String userPrincipalName;

    @Column(name = "office_location")
    private String officeLocation;

    @Column(name = "preferred_language")
    private String preferredLanguage;

    @Column(name = "business_phones")
    private List<String> businessPhones;
}
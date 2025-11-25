package com.tannguyen.ai.repository.third;

import java.util.List;

public interface FptFacetRepository {

    List<String> distinctOdataType();

    List<String> distinctDisplayName();

    List<String> distinctGivenName();

    List<String> distinctSurname();

    List<String> distinctJobTitle();

    List<String> distinctMail();

    List<String> distinctMobilePhone();

    List<String> distinctUserPrincipalName();

    List<String> distinctOfficeLocation();

    List<String> distinctPreferredLanguage();

    // Special case: TEXT[] array → return distinct entries (flatten array)
    List<String> distinctBusinessPhones();
}
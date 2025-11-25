package com.tannguyen.ai.repository.third;

import java.util.List;

public interface FptFacetRepository {

    List<String> distinctOdataType();

    List<String> distinctJobTitle();

    List<String> distinctOfficeLocation();

    List<String> distinctPreferredLanguage();
}
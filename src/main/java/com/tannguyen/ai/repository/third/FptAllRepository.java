package com.tannguyen.ai.repository.third;

import com.tannguyen.ai.model.third.fpt.FPTAll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FptAllRepository extends JpaRepository<FPTAll, String>,
        JpaSpecificationExecutor<FPTAll>, FptFacetRepository {

    @Query("select distinct f.odataType from FPTAll f where f.odataType is not null and f.odataType <> ''")
    List<String> distinctOdataType();

    @Query("select distinct f.displayName from FPTAll f where f.displayName is not null and f.displayName <> ''")
    List<String> distinctDisplayName();

    @Query("select distinct f.givenName from FPTAll f where f.givenName is not null and f.givenName <> ''")
    List<String> distinctGivenName();

    @Query("select distinct f.surname from FPTAll f where f.surname is not null and f.surname <> ''")
    List<String> distinctSurname();

    @Query("select distinct f.jobTitle from FPTAll f where f.jobTitle is not null and f.jobTitle <> ''")
    List<String> distinctJobTitle();

    @Query("select distinct f.mail from FPTAll f where f.mail is not null and f.mail <> ''")
    List<String> distinctMail();

    @Query("select distinct f.mobilePhone from FPTAll f where f.mobilePhone is not null and f.mobilePhone <> ''")
    List<String> distinctMobilePhone();

    @Query("select distinct f.userPrincipalName from FPTAll f where f.userPrincipalName is not null and f.userPrincipalName <> ''")
    List<String> distinctUserPrincipalName();

    @Query("select distinct f.officeLocation from FPTAll f where f.officeLocation is not null and f.officeLocation <> ''")
    List<String> distinctOfficeLocation();

    @Query("select distinct f.preferredLanguage from FPTAll f where f.preferredLanguage is not null and f.preferredLanguage <> ''")
    List<String> distinctPreferredLanguage();
}
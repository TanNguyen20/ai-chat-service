package com.tannguyen.ai.repository.third;

import com.tannguyen.ai.model.third.fpt.FPTISHCM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FISHCMRepository extends JpaRepository<FPTISHCM, String>,
        JpaSpecificationExecutor<FPTISHCM>, FptFacetRepository {

    @Query("select distinct f.odataType from FPTISHCM f where f.odataType is not null and f.odataType <> ''")
    List<String> distinctOdataType();

    @Query("select distinct f.displayName from FPTISHCM f where f.displayName is not null and f.displayName <> ''")
    List<String> distinctDisplayName();

    @Query("select distinct f.givenName from FPTISHCM f where f.givenName is not null and f.givenName <> ''")
    List<String> distinctGivenName();

    @Query("select distinct f.surname from FPTISHCM f where f.surname is not null and f.surname <> ''")
    List<String> distinctSurname();

    @Query("select distinct f.jobTitle from FPTISHCM f where f.jobTitle is not null and f.jobTitle <> ''")
    List<String> distinctJobTitle();

    @Query("select distinct f.mail from FPTISHCM f where f.mail is not null and f.mail <> ''")
    List<String> distinctMail();

    @Query("select distinct f.mobilePhone from FPTISHCM f where f.mobilePhone is not null and f.mobilePhone <> ''")
    List<String> distinctMobilePhone();

    @Query("select distinct f.userPrincipalName from FPTISHCM f where f.userPrincipalName is not null and f.userPrincipalName <> ''")
    List<String> distinctUserPrincipalName();

    @Query("select distinct f.officeLocation from FPTISHCM f where f.officeLocation is not null and f.officeLocation <> ''")
    List<String> distinctOfficeLocation();

    @Query("select distinct f.preferredLanguage from FPTISHCM f where f.preferredLanguage is not null and f.preferredLanguage <> ''")
    List<String> distinctPreferredLanguage();
}
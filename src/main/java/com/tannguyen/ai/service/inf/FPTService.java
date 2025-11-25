package com.tannguyen.ai.service.inf;

import com.tannguyen.ai.dto.response.FPTResponseDTO;
import com.tannguyen.ai.enums.FPTType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface FPTService {
    Page<FPTResponseDTO> list(
            FPTType fptType,
            List<String> odataType,
            List<String> displayName,
            List<String> givenName,
            List<String> surname,
            List<String> jobTitle,
            List<String> mail,
            List<String> mobilePhone,
            List<String> userPrincipalName,
            List<String> officeLocation,
            List<String> preferredLanguage,
            Pageable pageable
    );

    Page<FPTResponseDTO> search(
            FPTType fptType,
            String query,
            List<String> odataType,
            List<String> displayName,
            List<String> givenName,
            List<String> surname,
            List<String> jobTitle,
            List<String> mail,
            List<String> mobilePhone,
            List<String> userPrincipalName,
            List<String> officeLocation,
            List<String> preferredLanguage,
            Pageable pageable
    );

    Page<FPTResponseDTO> searchByName(
            FPTType fptType,
            String name,
            List<String> odataType,
            List<String> displayName,
            List<String> givenName,
            List<String> surname,
            List<String> jobTitle,
            List<String> mail,
            List<String> mobilePhone,
            List<String> userPrincipalName,
            List<String> officeLocation,
            List<String> preferredLanguage,
            Pageable pageable
    );

    FPTResponseDTO getById(FPTType fptType, String id);

    void deleteById(FPTType fptType, String id);

    Map<String, List<String>> facets(FPTType fptType);
}

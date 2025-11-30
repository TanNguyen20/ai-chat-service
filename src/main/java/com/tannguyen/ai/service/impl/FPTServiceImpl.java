package com.tannguyen.ai.service.impl;

import com.tannguyen.ai.dto.FPTColumnDTO;
import com.tannguyen.ai.dto.response.FPTResponseDTO;
import com.tannguyen.ai.enums.FPTType;
import com.tannguyen.ai.mapper.FPTMapper;
import com.tannguyen.ai.model.third.fpt.*;
import com.tannguyen.ai.repository.third.*;
import com.tannguyen.ai.service.inf.FPTService;
import com.tannguyen.ai.spec.FPTSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FPTServiceImpl implements FPTService {

    private final FptExtendRepository fptExtendRepository;
    private final FISRepository fisRepository;
    private final FptAllRepository fptAllRepository;
    private final FISHCMRepository fishcmRepository;

    // ---------------------------------------------------------
    // LIST
    // ---------------------------------------------------------
    @Override
    public Page<FPTResponseDTO> list(
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
    ) {

        return switch (fptType) {
            case FPT -> {
                Specification<FPTAll> spec = FPTSpecifications.filter(
                        odataType, displayName, givenName, surname, jobTitle,
                        mail, mobilePhone, userPrincipalName, officeLocation, preferredLanguage
                );
                yield fptAllRepository.findAll(spec, pageable).map(FPTMapper::toDto);
            }
            case FPT_EXTEND -> {
                Specification<FPTExtend> spec = FPTSpecifications.filter(
                        odataType, displayName, givenName, surname, jobTitle,
                        mail, mobilePhone, userPrincipalName, officeLocation, preferredLanguage
                );
                yield fptExtendRepository.findAll(spec, pageable).map(FPTMapper::toDto);
            }
            case FIS -> {
                Specification<FPTIS> spec = FPTSpecifications.filter(
                        odataType, displayName, givenName, surname, jobTitle,
                        mail, mobilePhone, userPrincipalName, officeLocation, preferredLanguage
                );
                yield fisRepository.findAll(spec, pageable).map(FPTMapper::toDto);
            }
            case FIS_HCM -> {
                Specification<FPTISHCM> spec = FPTSpecifications.filter(
                        odataType, displayName, givenName, surname, jobTitle,
                        mail, mobilePhone, userPrincipalName, officeLocation, preferredLanguage
                );
                yield fishcmRepository.findAll(spec, pageable).map(FPTMapper::toDto);
            }
        };
    }

    // ---------------------------------------------------------
    // SEARCH (free text + filters)
    // ---------------------------------------------------------
    @Override
    public Page<FPTResponseDTO> search(
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
    ) {

        return switch (fptType) {
            case FPT -> {
                Specification<FPTAll> spec = Specification
                        .where(FPTSpecifications.<FPTAll>freeText(query))
                        .and(FPTSpecifications.filter(
                                odataType, displayName, givenName, surname, jobTitle,
                                mail, mobilePhone, userPrincipalName, officeLocation, preferredLanguage
                        ));
                yield fptAllRepository.findAll(spec, pageable).map(FPTMapper::toDto);
            }
            case FPT_EXTEND -> {
                Specification<FPTExtend> spec = Specification
                        .where(FPTSpecifications.<FPTExtend>freeText(query))
                        .and(FPTSpecifications.filter(
                                odataType, displayName, givenName, surname, jobTitle,
                                mail, mobilePhone, userPrincipalName, officeLocation, preferredLanguage
                        ));
                yield fptExtendRepository.findAll(spec, pageable).map(FPTMapper::toDto);
            }
            case FIS -> {
                Specification<FPTIS> spec = Specification
                        .where(FPTSpecifications.<FPTIS>freeText(query))
                        .and(FPTSpecifications.filter(
                                odataType, displayName, givenName, surname, jobTitle,
                                mail, mobilePhone, userPrincipalName, officeLocation, preferredLanguage
                        ));
                yield fisRepository.findAll(spec, pageable).map(FPTMapper::toDto);
            }
            case FIS_HCM -> {
                Specification<FPTISHCM> spec = Specification
                        .where(FPTSpecifications.<FPTISHCM>freeText(query))
                        .and(FPTSpecifications.filter(
                                odataType, displayName, givenName, surname, jobTitle,
                                mail, mobilePhone, userPrincipalName, officeLocation, preferredLanguage
                        ));
                yield fishcmRepository.findAll(spec, pageable).map(FPTMapper::toDto);
            }
        };
    }

    // ---------------------------------------------------------
    // SEARCH BY NAME
    // ---------------------------------------------------------
    @Override
    public Page<FPTResponseDTO> searchByName(
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
    ) {

        return switch (fptType) {
            case FPT -> {
                Specification<FPTAll> spec = Specification
                        .where(FPTSpecifications.<FPTAll>containsIgnoreCase("displayName", name))
                        .and(FPTSpecifications.filter(
                                odataType, displayName, givenName, surname, jobTitle,
                                mail, mobilePhone, userPrincipalName, officeLocation, preferredLanguage
                        ));
                yield fptAllRepository.findAll(spec, pageable).map(FPTMapper::toDto);
            }
            case FPT_EXTEND -> {
                Specification<FPTExtend> spec = Specification
                        .where(FPTSpecifications.<FPTExtend>containsIgnoreCase("displayName", name))
                        .and(FPTSpecifications.filter(
                                odataType, displayName, givenName, surname, jobTitle,
                                mail, mobilePhone, userPrincipalName, officeLocation, preferredLanguage
                        ));
                yield fptExtendRepository.findAll(spec, pageable).map(FPTMapper::toDto);
            }
            case FIS -> {
                Specification<FPTIS> spec = Specification
                        .where(FPTSpecifications.<FPTIS>containsIgnoreCase("displayName", name))
                        .and(FPTSpecifications.filter(
                                odataType, displayName, givenName, surname, jobTitle,
                                mail, mobilePhone, userPrincipalName, officeLocation, preferredLanguage
                        ));
                yield fisRepository.findAll(spec, pageable).map(FPTMapper::toDto);
            }
            case FIS_HCM -> {
                Specification<FPTISHCM> spec = Specification
                        .where(FPTSpecifications.<FPTISHCM>containsIgnoreCase("displayName", name))
                        .and(FPTSpecifications.filter(
                                odataType, displayName, givenName, surname, jobTitle,
                                mail, mobilePhone, userPrincipalName, officeLocation, preferredLanguage
                        ));
                yield fishcmRepository.findAll(spec, pageable).map(FPTMapper::toDto);
            }
        };
    }

    @Override
    public FPTResponseDTO getById(FPTType fptType, String id) {
        return switch (fptType) {
            case FPT -> fptAllRepository.findById(id)
                    .map(FPTMapper::toDto)
                    .orElseThrow(() -> new IllegalArgumentException("FPT All not found: " + id));
            case FPT_EXTEND -> fptExtendRepository.findById(id)
                    .map(FPTMapper::toDto)
                    .orElseThrow(() -> new IllegalArgumentException("FPT Extend not found: " + id));
            case FIS -> fisRepository.findById(id)
                    .map(FPTMapper::toDto)
                    .orElseThrow(() -> new IllegalArgumentException("FIS not found: " + id));
            case FIS_HCM -> fishcmRepository.findById(id)
                    .map(FPTMapper::toDto)
                    .orElseThrow(() -> new IllegalArgumentException("FIS HCM not found: " + id));
        };
    }

    @Override
    public void deleteById(FPTType fptType, String id) {
        switch (fptType) {
            case FPT -> {
                if (fptAllRepository.existsById(id)) {
                    fptAllRepository.deleteById(id);
                }
            }
            case FPT_EXTEND -> {
                if (fptExtendRepository.existsById(id)) {
                    fptExtendRepository.deleteById(id);
                }
            }
            case FIS -> {
                if (fisRepository.existsById(id)) {
                    fisRepository.deleteById(id);
                }
            }
            case FIS_HCM -> {
                if (fishcmRepository.existsById(id)) {
                    fishcmRepository.deleteById(id);
                }
            }
        }
    }

    @Override
    public Map<String, List<String>> facets(FPTType fptType) {
        return switch (fptType) {
            case FPT -> toFacetMap(fptAllRepository);
            case FPT_EXTEND -> toFacetMap(fptExtendRepository);
            case FIS -> toFacetMap(fisRepository);
            case FIS_HCM -> toFacetMap(fishcmRepository);
        };
    }

    private Map<String, List<String>> toFacetMap(FptFacetRepository r) {
        return Map.of(
                "odataType", r.distinctOdataType(),
                "jobTitle", r.distinctJobTitle(),
                "officeLocation", r.distinctOfficeLocation(),
                "preferredLanguage", r.distinctPreferredLanguage()
        );
    }

    @Override
    public List<FPTColumnDTO> getColumns() {
        List<FPTColumnDTO> cols = new ArrayList<>();

        cols.add(new FPTColumnDTO("id", "ID", "text", false));
        cols.add(new FPTColumnDTO("displayName", "Display Name", "text", true));
        cols.add(new FPTColumnDTO("givenName", "Given Name", "text", true));
        cols.add(new FPTColumnDTO("surname", "Surname", "text", true));
        cols.add(new FPTColumnDTO("jobTitle", "Job Title", "text", true));
        cols.add(new FPTColumnDTO("mail", "Email", "email", true));
        cols.add(new FPTColumnDTO("mobilePhone", "Mobile Phone", "phone", false));
        cols.add(new FPTColumnDTO("userPrincipalName", "Username", "text", true));
        cols.add(new FPTColumnDTO("officeLocation", "Office", "text", true));
        cols.add(new FPTColumnDTO("preferredLanguage", "Language", "enum", false));
        cols.add(new FPTColumnDTO("businessPhones", "Business Phones", "array", false));
        cols.add(new FPTColumnDTO("odataType", "Type", "enum", true));

        return cols;
    }
}

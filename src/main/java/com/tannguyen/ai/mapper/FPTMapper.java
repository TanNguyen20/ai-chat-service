package com.tannguyen.ai.mapper;

import com.tannguyen.ai.dto.request.FPTRequestDTO;
import com.tannguyen.ai.dto.response.FPTResponseDTO;
import com.tannguyen.ai.model.third.base.FPTBase;
import com.tannguyen.ai.model.third.fpt.FPTExtend;

import java.util.function.Supplier;

public class FPTMapper {

    // -------------------- Entity from DTO --------------------
    public static FPTExtend toEntityFptExt(FPTRequestDTO dto) {
        if (dto == null) return null;
        FPTExtend entity = new FPTExtend();
        fillCommon(dto, entity);
        return entity;
    }

    public static <T extends FPTBase> T toEntity(FPTRequestDTO dto, Supplier<T> ctor) {
        if (dto == null) return null;
        T entity = ctor.get();
        fillCommon(dto, entity);
        return entity;
    }

    // -------------------- DTO from Entity --------------------
    public static FPTResponseDTO toDto(FPTBase entity) {
        if (entity == null) return null;
        FPTResponseDTO dto = new FPTResponseDTO();
        dto.setId(entity.getId());
        dto.setOdataType(entity.getOdataType());
        dto.setDisplayName(entity.getDisplayName());
        dto.setGivenName(entity.getGivenName());
        dto.setSurname(entity.getSurname());
        dto.setJobTitle(entity.getJobTitle());
        dto.setMail(entity.getMail());
        dto.setMobilePhone(entity.getMobilePhone());
        dto.setUserPrincipalName(entity.getUserPrincipalName());
        dto.setOfficeLocation(entity.getOfficeLocation());
        dto.setPreferredLanguage(entity.getPreferredLanguage());
        dto.setBusinessPhones(entity.getBusinessPhones());
        return dto;
    }

    // -------------------- Merge DTO into existing Entity --------------------
    public static void merge(FPTRequestDTO src, FPTBase dst) {
        if (src == null || dst == null) return;

        if (src.getOdataType() != null) dst.setOdataType(src.getOdataType());
        if (src.getDisplayName() != null) dst.setDisplayName(src.getDisplayName());
        if (src.getGivenName() != null) dst.setGivenName(src.getGivenName());
        if (src.getSurname() != null) dst.setSurname(src.getSurname());
        if (src.getJobTitle() != null) dst.setJobTitle(src.getJobTitle());
        if (src.getMail() != null) dst.setMail(src.getMail());
        if (src.getMobilePhone() != null) dst.setMobilePhone(src.getMobilePhone());
        if (src.getUserPrincipalName() != null) dst.setUserPrincipalName(src.getUserPrincipalName());
        if (src.getOfficeLocation() != null) dst.setOfficeLocation(src.getOfficeLocation());
        if (src.getPreferredLanguage() != null) dst.setPreferredLanguage(src.getPreferredLanguage());
        if (src.getBusinessPhones() != null) dst.setBusinessPhones(src.getBusinessPhones());
    }

    // -------------------- Internal helper --------------------
    private static void fillCommon(FPTRequestDTO dto, FPTBase entity) {
        entity.setId(dto.getId());
        entity.setOdataType(dto.getOdataType());
        entity.setDisplayName(dto.getDisplayName());
        entity.setGivenName(dto.getGivenName());
        entity.setSurname(dto.getSurname());
        entity.setJobTitle(dto.getJobTitle());
        entity.setMail(dto.getMail());
        entity.setMobilePhone(dto.getMobilePhone());
        entity.setUserPrincipalName(dto.getUserPrincipalName());
        entity.setOfficeLocation(dto.getOfficeLocation());
        entity.setPreferredLanguage(dto.getPreferredLanguage());
        entity.setBusinessPhones(dto.getBusinessPhones());
    }
}
package com.tannguyen.ai.dto;

public record CrudSetDTO(
        boolean create,
        boolean read,
        boolean update,
        boolean delete
) {}
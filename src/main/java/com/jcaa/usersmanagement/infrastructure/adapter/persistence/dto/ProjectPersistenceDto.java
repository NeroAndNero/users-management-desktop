package com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto;

public record ProjectPersistenceDto(
    String id,
    String name,
    String startDate,
    String endDate,
    String status,
    String promoterId,
    String createdAt,
    String updatedAt) {}
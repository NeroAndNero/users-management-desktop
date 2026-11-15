package com.jcaa.usersmanagement.infrastructure.adapter.persistence.entity;

public record ProjectEntity(
    String id,
    String name,
    String startDate,
    String endDate,
    String status,
    String promoterId,
    String createdAt,
    String updatedAt) {}
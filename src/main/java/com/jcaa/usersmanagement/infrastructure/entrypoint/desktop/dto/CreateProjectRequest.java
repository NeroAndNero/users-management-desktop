package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto;

public record CreateProjectRequest(
    String id,
    String name,
    String startDate,
    String endDate,
    String promoterId) {}
package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto;

public record UpdateProjectRequest(
    String id,
    String name,
    String startDate,
    String endDate,
    String status,
    String promoterId) {}
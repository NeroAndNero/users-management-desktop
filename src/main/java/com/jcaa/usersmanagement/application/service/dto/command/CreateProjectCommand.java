package com.jcaa.usersmanagement.application.service.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateProjectCommand(
    @NotBlank(message = "id must not be blank") String id,
    @NotBlank(message = "name must not be blank")
        @Size(min = 3, message = "name must have at least 3 characters")
        String name,
    @NotBlank(message = "start date must not be blank") String startDate,
    String endDate,
    @NotBlank(message = "promoter id must not be blank") String promoterId)
{

}

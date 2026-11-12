package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.query.GetProjectByIdQuery;
import com.jcaa.usersmanagement.domain.model.ProjectModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface GetProjectByIdUseCase {
  ProjectModel execute(@NotNull @Valid GetProjectByIdQuery query);
}
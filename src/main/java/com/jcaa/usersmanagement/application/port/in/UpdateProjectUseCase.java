package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.command.UpdateProjectCommand;
import com.jcaa.usersmanagement.domain.model.ProjectModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface UpdateProjectUseCase {
  ProjectModel execute(@NotNull @Valid UpdateProjectCommand command);
}
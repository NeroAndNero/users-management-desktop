package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.UpdateProjectUseCase;
import com.jcaa.usersmanagement.application.port.out.GetProjectByIdPort;
import com.jcaa.usersmanagement.application.port.out.UpdateProjectPort;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateProjectCommand;
import com.jcaa.usersmanagement.application.service.mapper.ProjectApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.ProjectNotFoundException;
import com.jcaa.usersmanagement.domain.model.ProjectModel;
import com.jcaa.usersmanagement.domain.valueobject.ProjectId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public final class UpdateProjectService implements UpdateProjectUseCase {

  private final UpdateProjectPort updateProjectPort;
  private final GetProjectByIdPort getProjectByIdPort;
  private final Validator validator;

  @Override
  public ProjectModel execute(final UpdateProjectCommand command) {
    validateCommand(command);

    final ProjectId projectId = new ProjectId(command.id());
    ensureProjectExists(projectId);

    // AQUÍ ESTÁ LA MAGIA: El mapper convierte el comando y valida el Enum
    final ProjectModel projectToUpdate = ProjectApplicationMapper.fromUpdateCommandToModel(command);
    return updateProjectPort.update(projectToUpdate);
  }

  private void validateCommand(final UpdateProjectCommand command) {
    final Set<ConstraintViolation<UpdateProjectCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  private void ensureProjectExists(final ProjectId projectId) {
    getProjectByIdPort
        .getById(projectId)
        .orElseThrow(() -> ProjectNotFoundException.becauseIdWasNotFound(projectId.value()));
  }
}
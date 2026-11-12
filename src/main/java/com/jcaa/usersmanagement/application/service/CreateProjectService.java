package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.CreateProjectUseCase;
import com.jcaa.usersmanagement.application.port.out.GetProjectByIdPort;
import com.jcaa.usersmanagement.application.port.out.SaveProjectPort;
import com.jcaa.usersmanagement.application.service.dto.command.CreateProjectCommand;
import com.jcaa.usersmanagement.application.service.mapper.ProjectApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.ProjectAlreadyExistsException;
import com.jcaa.usersmanagement.domain.model.ProjectModel;
import com.jcaa.usersmanagement.domain.valueobject.ProjectId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public final class CreateProjectService implements CreateProjectUseCase {

  private final SaveProjectPort saveProjectPort;
  private final GetProjectByIdPort getProjectByIdPort;
  private final Validator validator;

  @Override
  public ProjectModel execute(final CreateProjectCommand command) {
    validateCommand(command);

    final ProjectModel projectToSave = ProjectApplicationMapper.fromCreateCommandToModel(command);
    ensureProjectDoesNotExist(projectToSave.getId());

    return saveProjectPort.save(projectToSave);
  }

  private void validateCommand(final CreateProjectCommand command) {
    final Set<ConstraintViolation<CreateProjectCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  private void ensureProjectDoesNotExist(final ProjectId projectId) {
    getProjectByIdPort
        .getById(projectId)
        .ifPresent(
            ignored -> {
              throw ProjectAlreadyExistsException.becauseIdAlreadyExists(projectId.value());
            });
  }
}
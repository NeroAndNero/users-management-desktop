package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.DeleteProjectUseCase;
import com.jcaa.usersmanagement.application.port.out.DeleteProjectPort;
import com.jcaa.usersmanagement.application.port.out.GetProjectByIdPort;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteProjectCommand;
import com.jcaa.usersmanagement.application.service.mapper.ProjectApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.ProjectNotFoundException;
import com.jcaa.usersmanagement.domain.valueobject.ProjectId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public final class DeleteProjectService implements DeleteProjectUseCase {

  private final DeleteProjectPort deleteProjectPort;
  private final GetProjectByIdPort getProjectByIdPort;
  private final Validator validator;

  @Override
  public void execute(final DeleteProjectCommand command) {
    validateCommand(command);

    final ProjectId projectId = ProjectApplicationMapper.fromDeleteCommandToProjectId(command);
    ensureProjectExists(projectId);
    deleteProjectPort.delete(projectId);
  }

  private void validateCommand(final DeleteProjectCommand command) {
    final Set<ConstraintViolation<DeleteProjectCommand>> violations = validator.validate(command);
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
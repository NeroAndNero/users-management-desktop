package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.GetProjectByIdUseCase;
import com.jcaa.usersmanagement.application.port.out.GetProjectByIdPort;
import com.jcaa.usersmanagement.application.service.dto.query.GetProjectByIdQuery;
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
public final class GetProjectByIdService implements GetProjectByIdUseCase {

  private final GetProjectByIdPort getProjectByIdPort;
  private final Validator validator;

  @Override
  public ProjectModel execute(final GetProjectByIdQuery query) {
    validateQuery(query);

    final ProjectId projectId = ProjectApplicationMapper.fromGetProjectByIdQueryToProjectId(query);
    return getProjectByIdPort
        .getById(projectId)
        .orElseThrow(() -> ProjectNotFoundException.becauseIdWasNotFound(projectId.value()));
  }

  private void validateQuery(final GetProjectByIdQuery query) {
    final Set<ConstraintViolation<GetProjectByIdQuery>> violations = validator.validate(query);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
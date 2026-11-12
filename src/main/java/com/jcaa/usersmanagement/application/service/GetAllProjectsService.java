package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.GetAllProjectsUseCase;
import com.jcaa.usersmanagement.application.port.out.GetAllProjectsPort;
import com.jcaa.usersmanagement.domain.model.ProjectModel;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public final class GetAllProjectsService implements GetAllProjectsUseCase {

  private final GetAllProjectsPort getAllProjectsPort;

  @Override
  public List<ProjectModel> execute() {
    return getAllProjectsPort.getAll();
  }
}
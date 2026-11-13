package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller;

import com.jcaa.usersmanagement.application.port.in.CreateProjectUseCase;
import com.jcaa.usersmanagement.application.port.in.DeleteProjectUseCase;
import com.jcaa.usersmanagement.application.port.in.GetAllProjectsUseCase;
import com.jcaa.usersmanagement.application.port.in.GetProjectByIdUseCase;
import com.jcaa.usersmanagement.application.port.in.UpdateProjectUseCase;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateProjectRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.ProjectResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateProjectRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.mapper.ProjectDesktopMapper;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public final class ProjectController {

  private final CreateProjectUseCase createProjectUseCase;
  private final UpdateProjectUseCase updateProjectUseCase;
  private final DeleteProjectUseCase deleteProjectUseCase;
  private final GetProjectByIdUseCase getProjectByIdUseCase;
  private final GetAllProjectsUseCase getAllProjectsUseCase;

  public List<ProjectResponse> listAllProjects() {
    final var projects = getAllProjectsUseCase.execute();
    return ProjectDesktopMapper.toResponseList(projects);
  }

  public ProjectResponse findProjectById(final String id) {
    final var query = ProjectDesktopMapper.toGetByIdQuery(id);
    final var project = getProjectByIdUseCase.execute(query);
    return ProjectDesktopMapper.toResponse(project);
  }

  public ProjectResponse createProject(final CreateProjectRequest request) {
    final var command = ProjectDesktopMapper.toCreateCommand(request);
    final var project = createProjectUseCase.execute(command);
    return ProjectDesktopMapper.toResponse(project);
  }

  public ProjectResponse updateProject(final UpdateProjectRequest request) {
    final var command = ProjectDesktopMapper.toUpdateCommand(request);
    final var project = updateProjectUseCase.execute(command);
    return ProjectDesktopMapper.toResponse(project);
  }

  public void deleteProject(final String id) {
    final var command = ProjectDesktopMapper.toDeleteCommand(id);
    deleteProjectUseCase.execute(command);
  }
}
package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import com.jcaa.usersmanagement.application.port.out.DeleteProjectPort;
import com.jcaa.usersmanagement.application.port.out.GetAllProjectsPort;
import com.jcaa.usersmanagement.application.port.out.GetProjectByIdPort;
import com.jcaa.usersmanagement.application.port.out.SaveProjectPort;
import com.jcaa.usersmanagement.application.port.out.UpdateProjectPort;
import com.jcaa.usersmanagement.domain.exception.ProjectNotFoundException;
import com.jcaa.usersmanagement.domain.model.ProjectModel;
import com.jcaa.usersmanagement.domain.valueobject.ProjectId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class ProjectRepositoryMemory
    implements SaveProjectPort,
        UpdateProjectPort,
        GetProjectByIdPort,
        GetAllProjectsPort,
        DeleteProjectPort {

  private final Map<ProjectId, ProjectModel> storage = new ConcurrentHashMap<>();

  @Override
  public ProjectModel save(final ProjectModel project) {
    storage.put(project.getId(), project);
    return project;
  }

  @Override
  public ProjectModel update(final ProjectModel project) {
    if (!storage.containsKey(project.getId())) {
      throw ProjectNotFoundException.becauseIdWasNotFound(project.getId().value());
    }
    storage.put(project.getId(), project);
    return project;
  }

  @Override
  public Optional<ProjectModel> getById(final ProjectId projectId) {
    return Optional.ofNullable(storage.get(projectId));
  }

  @Override
  public List<ProjectModel> getAll() {
    return storage.values().stream()
        .sorted(Comparator.comparing(project -> project.getName().value(), String.CASE_INSENSITIVE_ORDER))
        .toList();
  }

  @Override
  public void delete(final ProjectId projectId) {
    storage.remove(projectId);
  }
}
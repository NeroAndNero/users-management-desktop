package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateProjectCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteProjectCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateProjectCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetProjectByIdQuery;
import com.jcaa.usersmanagement.domain.model.ProjectModel;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateProjectRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.ProjectResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateProjectRequest;
import java.util.List;
import java.util.Objects;

public final class ProjectDesktopMapper {

  private ProjectDesktopMapper() {}

  public static CreateProjectCommand toCreateCommand(final CreateProjectRequest request) {
    return new CreateProjectCommand(
        request.id(),
        request.name(),
        request.startDate(),
        request.endDate(),
        request.promoterId());
  }

  public static UpdateProjectCommand toUpdateCommand(final UpdateProjectRequest request) {
    return new UpdateProjectCommand(
        request.id(),
        request.name(),
        request.startDate(),
        request.endDate(),
        request.status(),
        request.promoterId());
  }

  public static DeleteProjectCommand toDeleteCommand(final String id) {
    return new DeleteProjectCommand(id);
  }

  public static GetProjectByIdQuery toGetByIdQuery(final String id) {
    return new GetProjectByIdQuery(id);
  }

  public static ProjectResponse toResponse(final ProjectModel project) {
    return new ProjectResponse(
        project.getId().value(),
        project.getName().value(),
        project.getDateRange().startDate().toString(),
        Objects.isNull(project.getDateRange().endDate())
            ? null
            : project.getDateRange().endDate().toString(),
        project.getStatus().name(),
        project.getPromoterId());
  }

  public static List<ProjectResponse> toResponseList(final List<ProjectModel> projects) {
    return projects.stream().map(ProjectDesktopMapper::toResponse).toList();
  }
}
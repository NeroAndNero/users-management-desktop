package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.application.port.in.CreateProjectUseCase;
import com.jcaa.usersmanagement.application.port.in.DeleteProjectUseCase;
import com.jcaa.usersmanagement.application.port.in.GetAllProjectsUseCase;
import com.jcaa.usersmanagement.application.port.in.GetProjectByIdUseCase;
import com.jcaa.usersmanagement.application.port.in.UpdateProjectUseCase;
import com.jcaa.usersmanagement.application.service.dto.command.CreateProjectCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteProjectCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateProjectCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetProjectByIdQuery;
import com.jcaa.usersmanagement.domain.model.ProjectModel;
import com.jcaa.usersmanagement.domain.valueobject.ProjectDateRange;
import com.jcaa.usersmanagement.domain.valueobject.ProjectId;
import com.jcaa.usersmanagement.domain.valueobject.ProjectName;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateProjectRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.ProjectResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateProjectRequest;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("ProjectController")
@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

  @Mock private CreateProjectUseCase createProjectUseCase;
  @Mock private UpdateProjectUseCase updateProjectUseCase;
  @Mock private DeleteProjectUseCase deleteProjectUseCase;
  @Mock private GetProjectByIdUseCase getProjectByIdUseCase;
  @Mock private GetAllProjectsUseCase getAllProjectsUseCase;
  @InjectMocks private ProjectController controller;

  private static ProjectModel project() {
    return ProjectModel.create(
        new ProjectId("PRJ-001"),
        new ProjectName("Portal Web"),
        new ProjectDateRange(LocalDate.of(2026, 1, 10), null),
        "1001");
  }

  @Test
  @DisplayName("createProject() delega al caso de uso y mapea la respuesta")
  void shouldCreateProject() {
    when(createProjectUseCase.execute(any(CreateProjectCommand.class))).thenReturn(project());

    final ProjectResponse response =
        controller.createProject(
            new CreateProjectRequest("PRJ-001", "Portal Web", "2026-01-10", null, "1001"));

    assertEquals("PRJ-001", response.id());
    assertEquals("PLANNED", response.status());
    assertEquals("2026-01-10", response.startDate());
    assertNull(response.endDate());
  }

  @Test
  @DisplayName("updateProject() delega al caso de uso")
  void shouldUpdateProject() {
    when(updateProjectUseCase.execute(any(UpdateProjectCommand.class))).thenReturn(project());

    final ProjectResponse response =
        controller.updateProject(
            new UpdateProjectRequest(
                "PRJ-001", "Portal Web", "2026-01-10", null, "PLANNED", "1001"));

    assertEquals("Portal Web", response.name());
  }

  @Test
  @DisplayName("findProjectById() delega al caso de uso")
  void shouldFindProjectById() {
    when(getProjectByIdUseCase.execute(any(GetProjectByIdQuery.class))).thenReturn(project());

    assertEquals("PRJ-001", controller.findProjectById("PRJ-001").id());
  }

  @Test
  @DisplayName("listAllProjects() mapea toda la lista")
  void shouldListAllProjects() {
    when(getAllProjectsUseCase.execute()).thenReturn(List.of(project()));

    assertEquals(1, controller.listAllProjects().size());
  }

  @Test
  @DisplayName("deleteProject() delega al caso de uso con el id")
  void shouldDeleteProject() {
    controller.deleteProject("PRJ-001");

    verify(deleteProjectUseCase).execute(new DeleteProjectCommand("PRJ-001"));
  }
}
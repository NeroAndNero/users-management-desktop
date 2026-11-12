package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.application.port.out.GetProjectByIdPort;
import com.jcaa.usersmanagement.application.port.out.UpdateProjectPort;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateProjectCommand;
import com.jcaa.usersmanagement.domain.enums.ProjectStatus;
import com.jcaa.usersmanagement.domain.exception.InvalidProjectStatusException;
import com.jcaa.usersmanagement.domain.exception.ProjectNotFoundException;
import com.jcaa.usersmanagement.domain.model.ProjectModel;
import com.jcaa.usersmanagement.domain.valueobject.ProjectDateRange;
import com.jcaa.usersmanagement.domain.valueobject.ProjectId;
import com.jcaa.usersmanagement.domain.valueobject.ProjectName;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("UpdateProjectService")
@ExtendWith(MockitoExtension.class)
class UpdateProjectServiceTest {

  @Mock private UpdateProjectPort updateProjectPort;
  @Mock private GetProjectByIdPort getProjectByIdPort;

  private UpdateProjectService service;

  @BeforeEach
  void setUp() {
    try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      service =
          new UpdateProjectService(
              updateProjectPort, getProjectByIdPort, validatorFactory.getValidator());
    }
  }

  private static ProjectModel existing() {
    return ProjectModel.create(
        new ProjectId("PRJ-001"),
        new ProjectName("Portal Web"),
        new ProjectDateRange(LocalDate.of(2026, 1, 10), null),
        "1001");
  }

  private static UpdateProjectCommand command(final String status) {
    return new UpdateProjectCommand(
        "PRJ-001", "Portal Web v2", "2026-01-10", "2026-06-30", status, "1002");
  }

  @Test
  @DisplayName("execute() actualiza el proyecto cuando existe")
  void shouldUpdateWhenProjectExists() {
    when(getProjectByIdPort.getById(any())).thenReturn(Optional.of(existing()));
    when(updateProjectPort.update(any(ProjectModel.class))).thenAnswer(inv -> inv.getArgument(0));

    final ProjectModel result = service.execute(command("FINISHED"));

    assertEquals("Portal Web v2", result.getName().value());
    assertEquals(ProjectStatus.FINISHED, result.getStatus());
    assertEquals("1002", result.getPromoterId());
    verify(updateProjectPort).update(any(ProjectModel.class));
  }

  @Test
  @DisplayName("execute() lanza ProjectNotFoundException cuando el id no existe")
  void shouldThrowWhenProjectNotFound() {
    when(getProjectByIdPort.getById(any())).thenReturn(Optional.empty());

    assertThrows(ProjectNotFoundException.class, () -> service.execute(command("FINISHED")));
    verify(updateProjectPort, never()).update(any());
  }

  @Test
  @DisplayName("execute() lanza InvalidProjectStatusException con un estado desconocido")
  void shouldThrowWhenStatusIsInvalid() {
    when(getProjectByIdPort.getById(any())).thenReturn(Optional.of(existing()));

    assertThrows(InvalidProjectStatusException.class, () -> service.execute(command("DONE")));
    verify(updateProjectPort, never()).update(any());
  }

  @Test
  @DisplayName("execute() lanza ConstraintViolationException cuando el command es inválido")
  void shouldThrowWhenCommandIsInvalid() {
    final UpdateProjectCommand invalid = new UpdateProjectCommand("", "", "", null, "", "");

    assertThrows(ConstraintViolationException.class, () -> service.execute(invalid));
    verify(updateProjectPort, never()).update(any());
  }
}
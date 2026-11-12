package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.application.port.out.GetProjectByIdPort;
import com.jcaa.usersmanagement.application.port.out.SaveProjectPort;
import com.jcaa.usersmanagement.application.service.dto.command.CreateProjectCommand;
import com.jcaa.usersmanagement.domain.enums.ProjectStatus;
import com.jcaa.usersmanagement.domain.exception.InvalidProjectDateRangeException;
import com.jcaa.usersmanagement.domain.exception.ProjectAlreadyExistsException;
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

@DisplayName("CreateProjectService")
@ExtendWith(MockitoExtension.class)
class CreateProjectServiceTest {

  @Mock private SaveProjectPort saveProjectPort;
  @Mock private GetProjectByIdPort getProjectByIdPort;

  private CreateProjectService service;

  @BeforeEach
  void setUp() {
    try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      service =
          new CreateProjectService(
              saveProjectPort, getProjectByIdPort, validatorFactory.getValidator());
    }
  }

  private static CreateProjectCommand validCommand() {
    return new CreateProjectCommand("PRJ-001", "Portal Web", "2026-01-10", null, "1001");
  }

  @Test
  @DisplayName("execute() guarda el proyecto como PLANNED cuando el id no existe")
  void shouldSaveProjectWhenIdDoesNotExist() {
    // Arrange
    when(getProjectByIdPort.getById(any())).thenReturn(Optional.empty());
    when(saveProjectPort.save(any(ProjectModel.class))).thenAnswer(inv -> inv.getArgument(0));

    // Act
    final ProjectModel result = service.execute(validCommand());

    // Assert
    assertEquals("PRJ-001", result.getId().value());
    assertEquals(ProjectStatus.PLANNED, result.getStatus());
    verify(saveProjectPort).save(any(ProjectModel.class));
  }

  @Test
  @DisplayName("execute() lanza ProjectAlreadyExistsException cuando el id ya existe")
  void shouldThrowWhenProjectAlreadyExists() {
    // Arrange
    final ProjectModel existing =
        ProjectModel.create(
            new ProjectId("PRJ-001"),
            new ProjectName("Portal Web"),
            new ProjectDateRange(LocalDate.of(2026, 1, 10), null),
            "1001");
    when(getProjectByIdPort.getById(any())).thenReturn(Optional.of(existing));

    // Act & Assert
    assertThrows(ProjectAlreadyExistsException.class, () -> service.execute(validCommand()));
    verify(saveProjectPort, never()).save(any());
  }

  @Test
  @DisplayName("execute() lanza ConstraintViolationException cuando el command es inválido")
  void shouldThrowWhenCommandIsInvalid() {
    final CreateProjectCommand invalid = new CreateProjectCommand("", "ab", "", null, "");

    assertThrows(ConstraintViolationException.class, () -> service.execute(invalid));
    verify(saveProjectPort, never()).save(any());
  }

  @Test
  @DisplayName("execute() lanza InvalidProjectDateRangeException cuando el fin es anterior al inicio")
  void shouldThrowWhenEndBeforeStart() {
    final CreateProjectCommand command =
        new CreateProjectCommand("PRJ-001", "Portal Web", "2026-02-01", "2026-01-01", "1001");

    assertThrows(InvalidProjectDateRangeException.class, () -> service.execute(command));
    verify(saveProjectPort, never()).save(any());
  }

  @Test
  @DisplayName("execute() lanza InvalidProjectDateRangeException cuando el formato de fecha es inválido")
  void shouldThrowWhenDateFormatIsInvalid() {
    final CreateProjectCommand command =
        new CreateProjectCommand("PRJ-001", "Portal Web", "10/01/2026", null, "1001");

    assertThrows(InvalidProjectDateRangeException.class, () -> service.execute(command));
    verify(saveProjectPort, never()).save(any());
  }
}
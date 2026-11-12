package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.application.port.out.DeleteProjectPort;
import com.jcaa.usersmanagement.application.port.out.GetProjectByIdPort;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteProjectCommand;
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

@DisplayName("DeleteProjectService")
@ExtendWith(MockitoExtension.class)
class DeleteProjectServiceTest {

  @Mock private DeleteProjectPort deleteProjectPort;
  @Mock private GetProjectByIdPort getProjectByIdPort;

  private DeleteProjectService service;

  @BeforeEach
  void setUp() {
    try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      service =
          new DeleteProjectService(
              deleteProjectPort, getProjectByIdPort, validatorFactory.getValidator());
    }
  }

  @Test
  @DisplayName("execute() invoca deleteProjectPort cuando el proyecto existe")
  void shouldDeleteWhenProjectExists() {
    final ProjectModel existing =
        ProjectModel.create(
            new ProjectId("PRJ-001"),
            new ProjectName("Portal Web"),
            new ProjectDateRange(LocalDate.of(2026, 1, 10), null),
            "1001");
    when(getProjectByIdPort.getById(any())).thenReturn(Optional.of(existing));

    service.execute(new DeleteProjectCommand("PRJ-001"));

    verify(deleteProjectPort).delete(new ProjectId("PRJ-001"));
  }

  @Test
  @DisplayName("execute() lanza ProjectNotFoundException cuando el id no existe")
  void shouldThrowWhenProjectNotFound() {
    when(getProjectByIdPort.getById(any())).thenReturn(Optional.empty());

    assertThrows(
        ProjectNotFoundException.class,
        () -> service.execute(new DeleteProjectCommand("no-existe")));
    verify(deleteProjectPort, never()).delete(any());
  }

  @Test
  @DisplayName("execute() lanza ConstraintViolationException cuando el id está vacío")
  void shouldThrowWhenIdIsBlank() {
    assertThrows(
        ConstraintViolationException.class, () -> service.execute(new DeleteProjectCommand("")));
    verify(deleteProjectPort, never()).delete(any());
  }
}
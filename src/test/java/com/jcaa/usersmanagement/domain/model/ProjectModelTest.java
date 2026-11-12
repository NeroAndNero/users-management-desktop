package com.jcaa.usersmanagement.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jcaa.usersmanagement.domain.enums.ProjectStatus;
import com.jcaa.usersmanagement.domain.valueobject.ProjectDateRange;
import com.jcaa.usersmanagement.domain.valueobject.ProjectId;
import com.jcaa.usersmanagement.domain.valueobject.ProjectName;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ProjectModel")
class ProjectModelTest {

  private static ProjectModel newProject() {
    return ProjectModel.create(
        new ProjectId("PRJ-001"),
        new ProjectName("Portal Web"),
        new ProjectDateRange(LocalDate.of(2026, 1, 10), null),
        "1001");
  }

  @Test
  @DisplayName("create() deja el proyecto en estado PLANNED")
  void shouldCreateAsPlanned() {
    assertEquals(ProjectStatus.PLANNED, newProject().getStatus());
  }

  @Test
  @DisplayName("start() devuelve una copia IN_PROGRESS y no modifica la original")
  void shouldStartWithoutMutatingOriginal() {
    final ProjectModel original = newProject();
    final ProjectModel started = original.start();

    assertEquals(ProjectStatus.IN_PROGRESS, started.getStatus());
    assertEquals(ProjectStatus.PLANNED, original.getStatus());
    assertEquals(original.getId(), started.getId());
  }

  @Test
  @DisplayName("finish() devuelve una copia FINISHED")
  void shouldFinish() {
    assertEquals(ProjectStatus.FINISHED, newProject().finish().getStatus());
  }

  @Test
  @DisplayName("cancel() devuelve una copia CANCELLED")
  void shouldCancel() {
    assertEquals(ProjectStatus.CANCELLED, newProject().cancel().getStatus());
  }
}
package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.jcaa.usersmanagement.domain.enums.ProjectStatus;
import com.jcaa.usersmanagement.domain.exception.InvalidProjectDateRangeException;
import com.jcaa.usersmanagement.domain.exception.InvalidProjectIdException;
import com.jcaa.usersmanagement.domain.exception.InvalidProjectNameException;
import com.jcaa.usersmanagement.domain.exception.InvalidProjectStatusException;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Project value objects")
class ProjectValueObjectsTest {

  @Test
  @DisplayName("ProjectId normaliza (trim) el valor")
  void shouldTrimProjectId() {
    assertEquals("PRJ-001", new ProjectId("  PRJ-001  ").value());
  }

  @Test
  @DisplayName("ProjectId lanza excepción cuando está vacío")
  void shouldRejectEmptyProjectId() {
    assertThrows(InvalidProjectIdException.class, () -> new ProjectId("   "));
  }

  @Test
  @DisplayName("ProjectName normaliza (trim) el valor")
  void shouldTrimProjectName() {
    assertEquals("Portal Web", new ProjectName("  Portal Web ").value());
  }

  @Test
  @DisplayName("ProjectName lanza excepción cuando está vacío")
  void shouldRejectEmptyProjectName() {
    assertThrows(InvalidProjectNameException.class, () -> new ProjectName(" "));
  }

  @Test
  @DisplayName("ProjectName lanza excepción cuando tiene menos de 3 caracteres")
  void shouldRejectShortProjectName() {
    assertThrows(InvalidProjectNameException.class, () -> new ProjectName("ab"));
  }

  @Test
  @DisplayName("ProjectDateRange acepta fecha de fin nula")
  void shouldAcceptNullEndDate() {
    final ProjectDateRange range = new ProjectDateRange(LocalDate.of(2026, 1, 10), null);
    assertNull(range.endDate());
  }

  @Test
  @DisplayName("ProjectDateRange acepta fin igual al inicio")
  void shouldAcceptSameDay() {
    final LocalDate day = LocalDate.of(2026, 1, 10);
    assertEquals(day, new ProjectDateRange(day, day).endDate());
  }

  @Test
  @DisplayName("ProjectDateRange lanza excepción cuando el fin es anterior al inicio")
  void shouldRejectEndBeforeStart() {
    assertThrows(
        InvalidProjectDateRangeException.class,
        () -> new ProjectDateRange(LocalDate.of(2026, 2, 1), LocalDate.of(2026, 1, 1)));
  }

    @Test
    @DisplayName("ProjectStatus.fromString ignora mayúsculas/minúsculas")
    void shouldParseStatusIgnoringCase() throws Exception {
        assertEquals(ProjectStatus.IN_PROGRESS, ProjectStatus.fromString("in_progress"));
    }

  @Test
  @DisplayName("ProjectStatus.fromString lanza excepción con valor desconocido")
  void shouldRejectUnknownStatus() {
    assertThrows(InvalidProjectStatusException.class, () -> ProjectStatus.fromString("DONE"));
  }
}
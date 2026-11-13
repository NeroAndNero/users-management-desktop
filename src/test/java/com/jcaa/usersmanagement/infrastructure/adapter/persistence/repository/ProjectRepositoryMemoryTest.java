package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.jcaa.usersmanagement.domain.exception.ProjectNotFoundException;
import com.jcaa.usersmanagement.domain.model.ProjectModel;
import com.jcaa.usersmanagement.domain.valueobject.ProjectDateRange;
import com.jcaa.usersmanagement.domain.valueobject.ProjectId;
import com.jcaa.usersmanagement.domain.valueobject.ProjectName;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ProjectRepositoryMemory")
class ProjectRepositoryMemoryTest {

  private ProjectRepositoryMemory repository;

  @BeforeEach
  void setUp() {
    repository = new ProjectRepositoryMemory();
  }

  private static ProjectModel project(final String id, final String name) {
    return ProjectModel.create(
        new ProjectId(id),
        new ProjectName(name),
        new ProjectDateRange(LocalDate.of(2026, 1, 10), null),
        "1001");
  }

  @Test
  @DisplayName("save() + getById() recuperan el proyecto guardado")
  void shouldSaveAndFindById() {
    final ProjectModel saved = repository.save(project("PRJ-001", "Portal Web"));

    assertEquals(saved, repository.getById(new ProjectId("PRJ-001")).orElseThrow());
  }

  @Test
  @DisplayName("getById() devuelve vacío cuando no existe")
  void shouldReturnEmptyWhenMissing() {
    assertTrue(repository.getById(new ProjectId("nope")).isEmpty());
  }

  @Test
  @DisplayName("update() reemplaza el proyecto existente")
  void shouldUpdateExisting() {
    repository.save(project("PRJ-001", "Portal Web"));

    repository.update(project("PRJ-001", "Portal Web v2"));

    assertEquals(
        "Portal Web v2",
        repository.getById(new ProjectId("PRJ-001")).orElseThrow().getName().value());
  }

  @Test
  @DisplayName("update() lanza ProjectNotFoundException cuando no existe")
  void shouldThrowWhenUpdatingMissing() {
    assertThrows(
        ProjectNotFoundException.class, () -> repository.update(project("PRJ-009", "Fantasma")));
  }

  @Test
  @DisplayName("delete() elimina el proyecto y es idempotente")
  void shouldDeleteIdempotently() {
    repository.save(project("PRJ-001", "Portal Web"));

    repository.delete(new ProjectId("PRJ-001"));
    repository.delete(new ProjectId("PRJ-001"));

    assertTrue(repository.getById(new ProjectId("PRJ-001")).isEmpty());
  }

  @Test
  @DisplayName("getAll() devuelve los proyectos ordenados por nombre sin distinguir mayúsculas")
  void shouldReturnAllSortedByName() {
    repository.save(project("PRJ-002", "zeta"));
    repository.save(project("PRJ-001", "Alfa"));
    repository.save(project("PRJ-003", "beta"));

    final List<String> names =
        repository.getAll().stream().map(p -> p.getName().value()).toList();

    assertEquals(List.of("Alfa", "beta", "zeta"), names);
  }
}
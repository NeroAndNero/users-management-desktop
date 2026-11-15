package com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.domain.enums.ProjectStatus;
import com.jcaa.usersmanagement.domain.model.ProjectModel;
import com.jcaa.usersmanagement.domain.valueobject.ProjectDateRange;
import com.jcaa.usersmanagement.domain.valueobject.ProjectId;
import com.jcaa.usersmanagement.domain.valueobject.ProjectName;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.ProjectPersistenceDto;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.entity.ProjectEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("ProjectPersistenceMapper")
@ExtendWith(MockitoExtension.class)
class ProjectPersistenceMapperTest {

  @Mock private ResultSet resultSet;

  private static ProjectModel model(final LocalDate end) {
    return new ProjectModel(
        new ProjectId("PRJ-001"),
        new ProjectName("Portal Web"),
        new ProjectDateRange(LocalDate.of(2026, 1, 10), end),
        ProjectStatus.IN_PROGRESS,
        "1001");
  }

  @Test
  @DisplayName("fromModelToDto() convierte fechas a texto ISO y deja las fechas de auditoría en null")
  void shouldMapModelToDto() throws Exception {
    final ProjectPersistenceDto dto =
        ProjectPersistenceMapper.fromModelToDto(model(LocalDate.of(2026, 6, 30)));

    assertEquals("PRJ-001", dto.id());
    assertEquals("2026-01-10", dto.startDate());
    assertEquals("2026-06-30", dto.endDate());
    assertEquals("IN_PROGRESS", dto.status());
    assertNull(dto.createdAt());
  }

  @Test
  @DisplayName("fromModelToDto() deja endDate en null cuando el proyecto no tiene fecha de fin")
  void shouldMapNullEndDate() throws Exception {
    assertNull(ProjectPersistenceMapper.fromModelToDto(model(null)).endDate());
  }

  @Test
  @DisplayName("fromEntityToModel() reconstruye el modelo de dominio")
  void shouldMapEntityToModel() throws Exception {
    final ProjectEntity entity =
        new ProjectEntity(
            "PRJ-001", "Portal Web", "2026-01-10", "2026-06-30", "FINISHED", "1001", "x", "y");

    final ProjectModel result = ProjectPersistenceMapper.fromEntityToModel(entity);

    assertEquals(ProjectStatus.FINISHED, result.getStatus());
    assertEquals(LocalDate.of(2026, 6, 30), result.getDateRange().endDate());
  }

  @Test
  @DisplayName("fromEntityToModel() acepta endDate nulo")
  void shouldMapEntityWithoutEndDate() throws Exception {
    final ProjectEntity entity =
        new ProjectEntity("PRJ-001", "Portal Web", "2026-01-10", null, "PLANNED", "1001", "x", "y");

    assertNull(ProjectPersistenceMapper.fromEntityToModel(entity).getDateRange().endDate());
  }

  @Test
  @DisplayName("fromEntityToModel() lanza excepción con una fecha corrupta en BD")
  void shouldFailOnCorruptDate() throws Exception {
    final ProjectEntity entity =
        new ProjectEntity("PRJ-001", "Portal Web", "no-es-fecha", null, "PLANNED", "1001", "x", "y");

    assertThrows(DateTimeException.class, () -> ProjectPersistenceMapper.fromEntityToModel(entity));
  }

  @Test
  @DisplayName("fromResultSetToModelList() devuelve un modelo por fila")
  void shouldMapResultSetToList() throws Exception {
    when(resultSet.next()).thenReturn(true, true, false);
    when(resultSet.getString("id")).thenReturn("PRJ-001", "PRJ-002");
    when(resultSet.getString("name")).thenReturn("Portal Web", "App Móvil");
    when(resultSet.getString("start_date")).thenReturn("2026-01-10", "2026-02-01");
    when(resultSet.getString("end_date")).thenReturn(null, "2026-08-01");
    when(resultSet.getString("status")).thenReturn("PLANNED", "FINISHED");
    when(resultSet.getString("promoter_id")).thenReturn("1001", "1002");
    when(resultSet.getString("created_at")).thenReturn("a", "b");
    when(resultSet.getString("updated_at")).thenReturn("a", "b");

    final List<ProjectModel> result = ProjectPersistenceMapper.fromResultSetToModelList(resultSet);

    assertEquals(2, result.size());
    assertEquals("PRJ-002", result.get(1).getId().value());
  }
}
package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.domain.enums.ProjectStatus;
import com.jcaa.usersmanagement.domain.exception.ProjectNotFoundException;
import com.jcaa.usersmanagement.domain.model.ProjectModel;
import com.jcaa.usersmanagement.domain.valueobject.ProjectDateRange;
import com.jcaa.usersmanagement.domain.valueobject.ProjectId;
import com.jcaa.usersmanagement.domain.valueobject.ProjectName;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.PersistenceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("ProjectRepositoryMySQL")
@ExtendWith(MockitoExtension.class)
class ProjectRepositoryMySQLTest {

  private static final String ID = "PRJ-001";

  @Mock private Connection connection;
  @Mock private PreparedStatement statement;
  @Mock private ResultSet resultSet;

  private ProjectRepositoryMySQL repository;
  private ProjectModel project;

  @BeforeEach
  void setUp() {
    repository = new ProjectRepositoryMySQL(connection);
    project =
        new ProjectModel(
            new ProjectId(ID),
            new ProjectName("Portal Web"),
            new ProjectDateRange(LocalDate.of(2026, 1, 10), null),
            ProjectStatus.PLANNED,
            "1001");
  }

  private void configureStatementAndResultSet() throws SQLException {
    when(connection.prepareStatement(anyString())).thenReturn(statement);
    when(statement.executeQuery()).thenReturn(resultSet);
  }

  private void configureResultSetRow() throws SQLException {
    when(resultSet.getString("id")).thenReturn(ID);
    when(resultSet.getString("name")).thenReturn("Portal Web");
    when(resultSet.getString("start_date")).thenReturn("2026-01-10");
    when(resultSet.getString("end_date")).thenReturn(null);
    when(resultSet.getString("status")).thenReturn("PLANNED");
    when(resultSet.getString("promoter_id")).thenReturn("1001");
    when(resultSet.getString("created_at")).thenReturn("2026-01-01");
    when(resultSet.getString("updated_at")).thenReturn("2026-01-01");
  }

  @Test
  @DisplayName("save() ejecuta INSERT y devuelve el proyecto leído por id")
  void shouldSaveAndReturnById() throws SQLException {
    configureStatementAndResultSet();
    when(resultSet.next()).thenReturn(true);
    configureResultSetRow();

    final ProjectModel result = repository.save(project);

    assertAll(
        () -> assertEquals(ID, result.getId().value()),
        () -> assertEquals("Portal Web", result.getName().value()));
    verify(statement).executeUpdate();
  }

  @Test
  @DisplayName("save() lanza PersistenceException cuando el INSERT falla")
  void shouldThrowWhenInsertFails() throws SQLException {
    when(connection.prepareStatement(anyString())).thenReturn(statement);
    when(statement.executeUpdate()).thenThrow(new SQLException("Insert failed"));

    assertThrows(PersistenceException.class, () -> repository.save(project));
  }

  @Test
  @DisplayName("save() lanza ProjectNotFoundException si no se encuentra tras guardar")
  void shouldThrowNotFoundAfterSave() throws SQLException {
    configureStatementAndResultSet();
    when(resultSet.next()).thenReturn(false);

    assertThrows(ProjectNotFoundException.class, () -> repository.save(project));
  }

  @Test
  @DisplayName("update() ejecuta UPDATE y devuelve el proyecto refrescado")
  void shouldUpdateAndReturnById() throws SQLException {
    configureStatementAndResultSet();
    when(resultSet.next()).thenReturn(true);
    configureResultSetRow();

    final ProjectModel result = repository.update(project);

    assertEquals(ID, result.getId().value());
    verify(statement).executeUpdate();
  }

  @Test
  @DisplayName("update() lanza PersistenceException cuando el UPDATE falla")
  void shouldThrowWhenUpdateFails() throws SQLException {
    when(connection.prepareStatement(anyString())).thenReturn(statement);
    when(statement.executeUpdate()).thenThrow(new SQLException("Update failed"));

    assertThrows(PersistenceException.class, () -> repository.update(project));
  }

  @Test
  @DisplayName("getById() devuelve el proyecto cuando existe la fila")
  void shouldReturnProjectWhenFound() throws SQLException {
    configureStatementAndResultSet();
    when(resultSet.next()).thenReturn(true);
    configureResultSetRow();

    final Optional<ProjectModel> result = repository.getById(new ProjectId(ID));

    assertTrue(result.isPresent());
    assertEquals(ID, result.get().getId().value());
  }

  @Test
  @DisplayName("getById() devuelve vacío cuando no existe la fila")
  void shouldReturnEmptyWhenNotFound() throws SQLException {
    configureStatementAndResultSet();
    when(resultSet.next()).thenReturn(false);

    assertTrue(repository.getById(new ProjectId(ID)).isEmpty());
  }

  @Test
  @DisplayName("getById() lanza PersistenceException cuando prepareStatement falla")
  void shouldThrowOnGetByIdPrepareFailure() throws SQLException {
    when(connection.prepareStatement(anyString())).thenThrow(new SQLException("down"));

    assertThrows(PersistenceException.class, () -> repository.getById(new ProjectId(ID)));
  }

  @Test
  @DisplayName("getAll() devuelve un modelo por fila")
  void shouldReturnAllProjects() throws SQLException {
    configureStatementAndResultSet();
    when(resultSet.next()).thenReturn(true, true, false);
    configureResultSetRow();

    final List<ProjectModel> result = repository.getAll();

    assertEquals(2, result.size());
  }

  @Test
  @DisplayName("getAll() lanza PersistenceException cuando la consulta falla")
  void shouldThrowOnGetAllFailure() throws SQLException {
    when(connection.prepareStatement(anyString())).thenThrow(new SQLException("down"));

    assertThrows(PersistenceException.class, () -> repository.getAll());
  }

  @Test
  @DisplayName("delete() ejecuta DELETE con el id")
  void shouldDeleteWithoutThrowing() throws SQLException {
    when(connection.prepareStatement(anyString())).thenReturn(statement);

    repository.delete(new ProjectId(ID));

    verify(statement).setString(1, ID);
    verify(statement).executeUpdate();
  }

  @Test
  @DisplayName("delete() lanza PersistenceException cuando el DELETE falla")
  void shouldThrowWhenDeleteFails() throws SQLException {
    when(connection.prepareStatement(anyString())).thenReturn(statement);
    when(statement.executeUpdate()).thenThrow(new SQLException("Delete failed"));

    assertThrows(PersistenceException.class, () -> repository.delete(new ProjectId(ID)));
  }
}
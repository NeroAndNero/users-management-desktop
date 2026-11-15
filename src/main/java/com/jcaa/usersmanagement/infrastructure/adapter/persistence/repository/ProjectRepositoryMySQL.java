package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import com.jcaa.usersmanagement.application.port.out.DeleteProjectPort;
import com.jcaa.usersmanagement.application.port.out.GetAllProjectsPort;
import com.jcaa.usersmanagement.application.port.out.GetProjectByIdPort;
import com.jcaa.usersmanagement.application.port.out.SaveProjectPort;
import com.jcaa.usersmanagement.application.port.out.UpdateProjectPort;
import com.jcaa.usersmanagement.domain.exception.ProjectNotFoundException;
import com.jcaa.usersmanagement.domain.model.ProjectModel;
import com.jcaa.usersmanagement.domain.valueobject.ProjectId;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.ProjectPersistenceDto;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.PersistenceException;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper.ProjectPersistenceMapper;
import lombok.RequiredArgsConstructor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public final class ProjectRepositoryMySQL
    implements SaveProjectPort,
        UpdateProjectPort,
        GetProjectByIdPort,
        GetAllProjectsPort,
        DeleteProjectPort {

  private static final String SQL_INSERT =
      "INSERT INTO projects "
      + "(id, name, start_date, end_date, status, promoter_id, created_at, updated_at) "
      + "VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())";

  private static final String SQL_UPDATE =
      "UPDATE projects SET name = ?, start_date = ?, end_date = ?, status = ?, "
      + "promoter_id = ?, updated_at = NOW() "
      + "WHERE id = ?";

  private static final String SQL_SELECT_BY_ID =
      "SELECT id, name, start_date, end_date, status, promoter_id, created_at, updated_at "
      + "FROM projects "
      + "WHERE id = ? LIMIT 1";

  private static final String SQL_SELECT_ALL =
      "SELECT id, name, start_date, end_date, status, promoter_id, created_at, updated_at "
      + "FROM projects "
      + "ORDER BY name ASC";

  private static final String SQL_DELETE =
      "DELETE FROM projects "
      + "WHERE id = ?";

  private final Connection connection;

  @Override
  public ProjectModel save(final ProjectModel project) {
    final ProjectPersistenceDto dto = ProjectPersistenceMapper.fromModelToDto(project);
    executeSave(dto);
    return findByIdOrFail(project.getId());
  }

  @Override
  public ProjectModel update(final ProjectModel project) {
    final ProjectPersistenceDto dto = ProjectPersistenceMapper.fromModelToDto(project);
    executeUpdate(dto);
    return findByIdOrFail(project.getId());
  }

  @Override
  public Optional<ProjectModel> getById(final ProjectId projectId) {
    try (final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
      statement.setString(1, projectId.value());
      final ResultSet resultSet = statement.executeQuery();
      if (!resultSet.next()) {
        return Optional.empty();
      }
      return Optional.of(ProjectPersistenceMapper.fromResultSetToModel(resultSet));
    } catch (final Exception exception) {
      throw PersistenceException.becauseProjectFindByIdFailed(projectId.value(), exception);
    }
  }

  @Override
  public List<ProjectModel> getAll() {
    try (final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL)) {
      final ResultSet resultSet = statement.executeQuery();
      return ProjectPersistenceMapper.fromResultSetToModelList(resultSet);
    } catch (final Exception exception) {
      throw PersistenceException.becauseProjectFindAllFailed(exception);
    }
  }

  @Override
  public void delete(final ProjectId projectId) {
    try (final PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
      statement.setString(1, projectId.value());
      statement.executeUpdate();
    } catch (final SQLException exception) {
      throw PersistenceException.becauseProjectDeleteFailed(projectId.value(), exception);
    }
  }

  private void executeSave(final ProjectPersistenceDto dto) {
    try (final PreparedStatement statement = connection.prepareStatement(SQL_INSERT)) {
      statement.setString(1, dto.id());
      statement.setString(2, dto.name());
      statement.setString(3, dto.startDate());
      statement.setString(4, dto.endDate());
      statement.setString(5, dto.status());
      statement.setString(6, dto.promoterId());
      statement.executeUpdate();
    } catch (final SQLException exception) {
      throw PersistenceException.becauseProjectSaveFailed(dto.id(), exception);
    }
  }

  private void executeUpdate(final ProjectPersistenceDto dto) {
    try (final PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
      statement.setString(1, dto.name());
      statement.setString(2, dto.startDate());
      statement.setString(3, dto.endDate());
      statement.setString(4, dto.status());
      statement.setString(5, dto.promoterId());
      statement.setString(6, dto.id());
      statement.executeUpdate();
    } catch (final SQLException exception) {
      throw PersistenceException.becauseProjectUpdateFailed(dto.id(), exception);
    }
  }

  private ProjectModel findByIdOrFail(final ProjectId projectId) {
    return getById(projectId)
        .orElseThrow(() -> ProjectNotFoundException.becauseIdWasNotFound(projectId.value()));
  }
}
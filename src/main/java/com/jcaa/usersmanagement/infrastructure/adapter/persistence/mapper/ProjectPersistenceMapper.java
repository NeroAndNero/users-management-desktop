package com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper;

import com.jcaa.usersmanagement.domain.enums.ProjectStatus;
import com.jcaa.usersmanagement.domain.model.ProjectModel;
import com.jcaa.usersmanagement.domain.valueobject.ProjectDateRange;
import com.jcaa.usersmanagement.domain.valueobject.ProjectId;
import com.jcaa.usersmanagement.domain.valueobject.ProjectName;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.ProjectPersistenceDto;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.entity.ProjectEntity;
import lombok.experimental.UtilityClass;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class ProjectPersistenceMapper {

  public ProjectPersistenceDto fromModelToDto(final ProjectModel project) {
    final LocalDate endDate = project.getDateRange().endDate();
    return new ProjectPersistenceDto(
        project.getId().value(),
        project.getName().value(),
        project.getDateRange().startDate().toString(),
        Objects.isNull(endDate) ? null : endDate.toString(),
        project.getStatus().name(),
        project.getPromoterId(),
        null,
        null);
  }

  public ProjectEntity fromResultSetToEntity(final ResultSet resultSet) throws SQLException {
    return new ProjectEntity(
        resultSet.getString("id"),
        resultSet.getString("name"),
        resultSet.getString("start_date"),
        resultSet.getString("end_date"),
        resultSet.getString("status"),
        resultSet.getString("promoter_id"),
        resultSet.getString("created_at"),
        resultSet.getString("updated_at"));
  }

  public ProjectModel fromEntityToModel(final ProjectEntity entity) {
    final LocalDate endDate =
        Objects.isNull(entity.endDate()) ? null : LocalDate.parse(entity.endDate());
    return new ProjectModel(
        new ProjectId(entity.id()),
        new ProjectName(entity.name()),
        new ProjectDateRange(LocalDate.parse(entity.startDate()), endDate),
        ProjectStatus.fromString(entity.status()),
        entity.promoterId());
  }

  public ProjectModel fromResultSetToModel(final ResultSet resultSet) throws SQLException {
    return fromEntityToModel(fromResultSetToEntity(resultSet));
  }

  public List<ProjectModel> fromResultSetToModelList(final ResultSet resultSet)
      throws SQLException {
    final List<ProjectModel> projects = new ArrayList<>();
    while (resultSet.next()) {
      projects.add(fromResultSetToModel(resultSet));
    }
    return projects;
  }
}
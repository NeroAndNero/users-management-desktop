package com.jcaa.usersmanagement.application.service.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateProjectCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteProjectCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateProjectCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetProjectByIdQuery;
import com.jcaa.usersmanagement.domain.enums.ProjectStatus;
import com.jcaa.usersmanagement.domain.exception.InvalidProjectDateRangeException;
import com.jcaa.usersmanagement.domain.model.ProjectModel;
import com.jcaa.usersmanagement.domain.valueobject.ProjectDateRange;
import com.jcaa.usersmanagement.domain.valueobject.ProjectId;
import com.jcaa.usersmanagement.domain.valueobject.ProjectName;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProjectApplicationMapper {

  public ProjectModel fromCreateCommandToModel(final CreateProjectCommand command) {
    return ProjectModel.create(
        new ProjectId(command.id()),
        new ProjectName(command.name()),
        buildDateRange(command.startDate(), command.endDate()),
        command.promoterId().trim());
  }

  public ProjectModel fromUpdateCommandToModel(final UpdateProjectCommand command) throws Exception {
    return new ProjectModel(
        new ProjectId(command.id()),
        new ProjectName(command.name()),
        buildDateRange(command.startDate(), command.endDate()),
        ProjectStatus.fromString(command.status()),
        command.promoterId().trim());
  }

  public ProjectId fromGetProjectByIdQueryToProjectId(final GetProjectByIdQuery query) {
    return new ProjectId(query.id());
  }

  public ProjectId fromDeleteCommandToProjectId(final DeleteProjectCommand command) {
    return new ProjectId(command.id());
  }

  private ProjectDateRange buildDateRange(final String startDate, final String endDate) {
    return new ProjectDateRange(parseDate(startDate), parseOptionalDate(endDate));
  }

  private LocalDate parseDate(final String value) {
    try {
      return LocalDate.parse(value.trim());
    } catch (final DateTimeParseException exception) {
      throw InvalidProjectDateRangeException.becauseFormatIsInvalid(value);
    }
  }

  private LocalDate parseOptionalDate(final String value) {
    if (Objects.isNull(value) || value.isBlank()) {
      return null;
    }
    return parseDate(value);
  }
}
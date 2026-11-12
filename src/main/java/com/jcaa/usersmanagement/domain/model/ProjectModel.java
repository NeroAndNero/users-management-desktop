package com.jcaa.usersmanagement.domain.model;

import com.jcaa.usersmanagement.domain.enums.ProjectStatus;
import com.jcaa.usersmanagement.domain.valueobject.ProjectDateRange;
import com.jcaa.usersmanagement.domain.valueobject.ProjectId;
import com.jcaa.usersmanagement.domain.valueobject.ProjectName;
import lombok.Value;

@Value
public class ProjectModel {

  ProjectId id;
  ProjectName name;
  ProjectDateRange dateRange;
  ProjectStatus status;
  String promoterId;

  public static ProjectModel create(
      final ProjectId id,
      final ProjectName name,
      final ProjectDateRange dateRange,
      final String promoterId) {
    return new ProjectModel(id, name, dateRange, ProjectStatus.PLANNED, promoterId);
  }

  public ProjectModel start() {
    return new ProjectModel(id, name, dateRange, ProjectStatus.IN_PROGRESS, promoterId);
  }

  public ProjectModel finish() {
    return new ProjectModel(id, name, dateRange, ProjectStatus.FINISHED, promoterId);
  }

  public ProjectModel cancel() {
    return new ProjectModel(id, name, dateRange, ProjectStatus.CANCELLED, promoterId);
  }
}
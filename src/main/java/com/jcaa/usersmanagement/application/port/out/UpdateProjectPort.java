package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.ProjectModel;

public interface UpdateProjectPort {
  ProjectModel update(ProjectModel project);
}
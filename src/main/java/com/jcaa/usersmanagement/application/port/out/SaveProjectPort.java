package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.ProjectModel;

public interface SaveProjectPort {
  ProjectModel save(ProjectModel project);
}
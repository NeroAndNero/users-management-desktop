package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.ProjectModel;
import com.jcaa.usersmanagement.domain.valueobject.ProjectId;
import java.util.Optional;

public interface GetProjectByIdPort {
  Optional<ProjectModel> getById(ProjectId projectId);
}
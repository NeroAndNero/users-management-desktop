package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.valueobject.ProjectId;

public interface DeleteProjectPort {
  void delete(ProjectId projectId);
}
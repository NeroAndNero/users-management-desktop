package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.domain.model.ProjectModel;
import java.util.List;

public interface GetAllProjectsUseCase {
  List<ProjectModel> execute();
}
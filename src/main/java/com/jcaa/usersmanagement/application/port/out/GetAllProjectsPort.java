package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.ProjectModel;
import java.util.List;

public interface GetAllProjectsPort {
  List<ProjectModel> getAll();
}
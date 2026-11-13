package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ProjectResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.ProjectController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.ProjectResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ListProjectsHandler implements OperationHandler {

  private final ProjectController projectController;
  private final ProjectResponsePrinter printer;

  @Override
  public void handle() {
    final List<ProjectResponse> projects = projectController.listAllProjects();
    printer.printList(projects);
  }
}
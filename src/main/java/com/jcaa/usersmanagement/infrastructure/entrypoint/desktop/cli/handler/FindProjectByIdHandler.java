package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.ProjectNotFoundException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ProjectResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.ProjectController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.ProjectResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FindProjectByIdHandler implements OperationHandler {

  private final ProjectController projectController;
  private final ConsoleIO console;
  private final ProjectResponsePrinter printer;

  @Override
  public void handle() {
    final String id = console.readRequired("Project ID: ");
    try {
      final ProjectResponse project = projectController.findProjectById(id);
      printer.print(project);
    } catch (final ProjectNotFoundException exception) {
      console.println("  Not found: " + exception.getMessage());
    }
  }
}
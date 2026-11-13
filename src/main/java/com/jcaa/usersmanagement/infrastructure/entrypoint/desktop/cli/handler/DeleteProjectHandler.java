package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.ProjectNotFoundException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.ProjectController;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class DeleteProjectHandler implements OperationHandler {

  private final ProjectController projectController;
  private final ConsoleIO console;

  @Override
  public void handle() {
    final String id = console.readRequired("Project ID to delete: ");
    try {
      projectController.deleteProject(id);
      console.println("  Project deleted successfully.");
    } catch (final ProjectNotFoundException exception) {
      console.println("  Not found: " + exception.getMessage());
    }
  }
}
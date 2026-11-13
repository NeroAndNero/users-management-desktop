package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.ProjectNotFoundException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ProjectResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.ProjectController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.ProjectResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateProjectRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateProjectHandler implements OperationHandler {

  private final ProjectController projectController;
  private final ConsoleIO console;
  private final ProjectResponsePrinter printer;

  @Override
  public void handle() {
    final String id         = console.readRequired("Project ID                                      : ");
    final String name       = console.readRequired("New commercial name                             : ");
    final String startDate  = console.readRequired("Start date (yyyy-MM-dd)                         : ");
    final String endDate    = console.readOptional("End date (yyyy-MM-dd, blank if none)            : ");
    final String status     = console.readRequired("Status (PLANNED / IN_PROGRESS / FINISHED / CANCELLED): ");
    final String promoterId = console.readRequired("Promoter ID (employee DNI)                      : ");

    try {
      final ProjectResponse updated =
          projectController.updateProject(
              new UpdateProjectRequest(
                  id, name, startDate, endDate.isBlank() ? null : endDate, status, promoterId));
      console.println("\n  Project updated successfully.");
      printer.print(updated);
    } catch (final ProjectNotFoundException exception) {
      console.println("  Not found: " + exception.getMessage());
    }
  }
}
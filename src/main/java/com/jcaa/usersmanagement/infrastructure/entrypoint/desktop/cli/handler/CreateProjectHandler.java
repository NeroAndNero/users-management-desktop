package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.ProjectAlreadyExistsException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ProjectResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.ProjectController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateProjectRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.ProjectResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CreateProjectHandler implements OperationHandler {

  private final ProjectController projectController;
  private final ConsoleIO console;
  private final ProjectResponsePrinter printer;

  @Override
  public void handle() {
    final String id         = console.readRequired("Code name (ID)                       : ");
    final String name       = console.readRequired("Commercial name                      : ");
    final String startDate  = console.readRequired("Start date (yyyy-MM-dd)              : ");
    final String endDate    = console.readOptional("End date (yyyy-MM-dd, blank if none) : ");
    final String promoterId = console.readRequired("Promoter ID (employee DNI)           : ");

    try {
      final ProjectResponse created =
          projectController.createProject(
              new CreateProjectRequest(
                  id, name, startDate, endDate.isBlank() ? null : endDate, promoterId));
      console.println("\n  Project created successfully.");
      printer.print(created);
    } catch (final ProjectAlreadyExistsException exception) {
      console.println("  Error: " + exception.getMessage());
    }
  }
}
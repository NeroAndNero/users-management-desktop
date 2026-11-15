package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli;

import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler.*;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ProjectResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.UserResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.menu.MenuOption;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.ProjectController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.UserController;
import jakarta.validation.ConstraintViolationException;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UserManagementCli {

  private static final String BANNER =
      """
      ==========================================
           Users Management System
      ==========================================""";

  private static final String MENU_BORDER = "  ==========================================";

  private final UserController userController;
  private final ProjectController projectController;
  private final ConsoleIO console;

  public void start() {
    console.println(BANNER);
    final UserResponsePrinter printer = new UserResponsePrinter(console);
    final ProjectResponsePrinter projectPrinter = new ProjectResponsePrinter(console);
    runLoop(buildHandlers(printer, projectPrinter));
  }

  private void runLoop(final Map<MenuOption, OperationHandler> handlers) {
    boolean running = true;
    while (running) {
      printMenu();
      final int choice = console.readInt("\n  Option: ");
      final Optional<MenuOption> option = MenuOption.fromNumber(choice);

      if (option.isEmpty()) {
        console.println("  Invalid option. Please try again.");
      } else if (option.get() == MenuOption.EXIT) {
        console.println("\n  Goodbye!\n");
        running = false;
      } else {
        executeHandler(handlers, option.get());
      }
    }
  }

  private void executeHandler(
      final Map<MenuOption, OperationHandler> handlers, final MenuOption option) {
    try {
      handlers.get(option).handle();
    } catch (final ConstraintViolationException exception) {
      console.println("  Validation errors:");
      exception.getConstraintViolations()
          .forEach(violation -> console.println("    - " + violation.getMessage()));
    } catch (final RuntimeException exception) {
      console.println("  Unexpected error: " + exception.getMessage());
    }
  }

  private Map<MenuOption, OperationHandler> buildHandlers(
      final UserResponsePrinter printer, final ProjectResponsePrinter projectPrinter) {
    return Map.ofEntries(
        Map.entry(MenuOption.LIST_USERS, new ListUsersHandler(userController, printer)),
        Map.entry(MenuOption.FIND_USER, new FindUserByIdHandler(userController, console, printer)),
        Map.entry(MenuOption.CREATE_USER, new CreateUserHandler(userController, console, printer)),
        Map.entry(MenuOption.UPDATE_USER, new UpdateUserHandler(userController, console, printer)),
        Map.entry(MenuOption.DELETE_USER, new DeleteUserHandler(userController, console)),
        Map.entry(MenuOption.LOGIN, new LoginHandler(userController, console, printer)),
        Map.entry(MenuOption.LIST_PROJECTS, new ListProjectsHandler(projectController, projectPrinter)),
        Map.entry(MenuOption.FIND_PROJECT, new FindProjectByIdHandler(projectController, console, projectPrinter)),
        Map.entry(MenuOption.CREATE_PROJECT, new CreateProjectHandler(projectController, console, projectPrinter)),
        Map.entry(MenuOption.UPDATE_PROJECT, new UpdateProjectHandler(projectController, console, projectPrinter)),
        Map.entry(MenuOption.DELETE_PROJECT, new DeleteProjectHandler(projectController, console)));
  }

  private void printMenu() {
    console.println();
    console.println(MENU_BORDER);
    console.println("    Main Menu");
    console.println(MENU_BORDER);
    for (final MenuOption option : MenuOption.values()) {
      console.printf("    [%d] %s%n", option.getNumber(), option.getDescription());
    }
    console.println(MENU_BORDER);
  }
}
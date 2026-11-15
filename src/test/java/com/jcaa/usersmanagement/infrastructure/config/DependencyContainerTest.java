package com.jcaa.usersmanagement.infrastructure.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.PersistenceException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.ProjectController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.UserController;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("DependencyContainer")
@ExtendWith(MockitoExtension.class)
class DependencyContainerTest {

  @Mock private Connection mockConnection;

  @Test
  @DisplayName("constructor wires all dependencies and userController() returns a non-null UserController")
  void shouldWireAllDependenciesAndExposeNonNullUserController() {
    try (final MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
      mockedDriverManager
          .when(() -> DriverManager.getConnection(any(String.class), any(String.class), any(String.class)))
          .thenReturn(mockConnection);

      final DependencyContainer container = new DependencyContainer();

      assertNotNull(container.userController(), "userController() must return a non-null UserController after successful construction");
    }
  }

  @Test
  @DisplayName("userController() returns the same UserController instance on every call")
  void shouldReturnTheSameUserControllerInstanceOnEveryCall() {
    try (final MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
      mockedDriverManager
          .when(() -> DriverManager.getConnection(any(String.class), any(String.class), any(String.class)))
          .thenReturn(mockConnection);
      final DependencyContainer container = new DependencyContainer();

      final UserController first = container.userController();
      final UserController second = container.userController();

      assertSame(first, second, "userController() must return the same instance on every call");
    }
  }

  @Test
  @DisplayName("projectController() returns a non-null ProjectController, the same on every call")
  void shouldExposeTheSameNonNullProjectControllerOnEveryCall() {
    try (final MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
      mockedDriverManager
          .when(() -> DriverManager.getConnection(any(String.class), any(String.class), any(String.class)))
          .thenReturn(mockConnection);
      final DependencyContainer container = new DependencyContainer();

      final ProjectController first = container.projectController();
      final ProjectController second = container.projectController();

      assertNotNull(first, "projectController() must return a non-null ProjectController");
      assertSame(first, second, "projectController() must return the same instance on every call");
    }
  }

  @Test
  @DisplayName("constructor propagates PersistenceException when the database connection fails")
  void shouldPropagatePersistenceExceptionWhenDatabaseConnectionFails() {
    final SQLException cause = new SQLException("Connection refused");
    try (final MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
      mockedDriverManager
          .when(() -> DriverManager.getConnection(any(String.class), any(String.class), any(String.class)))
          .thenThrow(cause);

      assertThrows(PersistenceException.class, DependencyContainer::new, "PersistenceException must propagate without wrapping when DriverManager throws SQLException");
    }
  }
}
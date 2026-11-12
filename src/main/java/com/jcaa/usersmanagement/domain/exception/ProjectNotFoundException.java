package com.jcaa.usersmanagement.domain.exception;

public final class ProjectNotFoundException extends DomainException {

  private static final String MESSAGE_BY_ID = "The project with id '%s' was not found.";

  private ProjectNotFoundException(final String message) {
    super(message);
  }

  public static ProjectNotFoundException becauseIdWasNotFound(final String projectId) {
    return new ProjectNotFoundException(String.format(MESSAGE_BY_ID, projectId));
  }
}
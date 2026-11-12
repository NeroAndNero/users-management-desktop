package com.jcaa.usersmanagement.domain.exception;

public final class ProjectAlreadyExistsException extends DomainException {

  private static final String MESSAGE_ID_EXISTS = "A project with id '%s' already exists.";

  private ProjectAlreadyExistsException(final String message) {
    super(message);
  }

  public static ProjectAlreadyExistsException becauseIdAlreadyExists(final String projectId) {
    return new ProjectAlreadyExistsException(String.format(MESSAGE_ID_EXISTS, projectId));
  }
}
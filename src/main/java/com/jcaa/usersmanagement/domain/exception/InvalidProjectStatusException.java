package com.jcaa.usersmanagement.domain.exception;

public final class InvalidProjectStatusException extends DomainException {

  private static final String MESSAGE_INVALID = "The project status '%s' is not valid.";

  private InvalidProjectStatusException(final String message) {
    super(message);
  }

  public static InvalidProjectStatusException becauseValueIsInvalid(final String status) {
    return new InvalidProjectStatusException(String.format(MESSAGE_INVALID, status));
  }
}
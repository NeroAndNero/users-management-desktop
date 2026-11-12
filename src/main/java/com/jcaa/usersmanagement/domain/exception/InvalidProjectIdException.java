package com.jcaa.usersmanagement.domain.exception;

public final class InvalidProjectIdException extends DomainException {

  private static final String MESSAGE_EMPTY = "The project id must not be empty.";

  private InvalidProjectIdException(final String message) {
    super(message);
  }

  public static InvalidProjectIdException becauseValueIsEmpty() {
    return new InvalidProjectIdException(MESSAGE_EMPTY);
  }
}
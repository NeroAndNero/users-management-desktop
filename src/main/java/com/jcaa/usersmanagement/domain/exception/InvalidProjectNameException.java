package com.jcaa.usersmanagement.domain.exception;

public final class InvalidProjectNameException extends DomainException {

  private static final String MESSAGE_EMPTY = "The project name must not be empty.";
  private static final String MESSAGE_TOO_SHORT =
      "The project name must have at least %d characters.";

  private InvalidProjectNameException(final String message) {
    super(message);
  }

  public static InvalidProjectNameException becauseValueIsEmpty() {
    return new InvalidProjectNameException(MESSAGE_EMPTY);
  }

  public static InvalidProjectNameException becauseLengthIsTooShort(final int minimumLength) {
    return new InvalidProjectNameException(String.format(MESSAGE_TOO_SHORT, minimumLength));
  }
}
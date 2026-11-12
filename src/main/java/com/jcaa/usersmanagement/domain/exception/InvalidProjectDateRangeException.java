package com.jcaa.usersmanagement.domain.exception;

import java.time.LocalDate;

public final class InvalidProjectDateRangeException extends DomainException {

  private static final String MESSAGE_END_BEFORE_START =
      "The project end date '%s' must not be before the start date '%s'.";
  private static final String MESSAGE_INVALID_FORMAT =
      "The date '%s' is not valid. Use the format yyyy-MM-dd.";

  private InvalidProjectDateRangeException(final String message) {
    super(message);
  }

  public static InvalidProjectDateRangeException becauseEndIsBeforeStart(
      final LocalDate startDate, final LocalDate endDate) {
    return new InvalidProjectDateRangeException(
        String.format(MESSAGE_END_BEFORE_START, endDate, startDate));
  }

  public static InvalidProjectDateRangeException becauseFormatIsInvalid(final String value) {
    return new InvalidProjectDateRangeException(String.format(MESSAGE_INVALID_FORMAT, value));
  }
}
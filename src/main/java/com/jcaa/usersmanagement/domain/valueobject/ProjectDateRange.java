package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidProjectDateRangeException;
import java.time.LocalDate;
import java.util.Objects;

public record ProjectDateRange(LocalDate startDate, LocalDate endDate) {

  public ProjectDateRange {
    Objects.requireNonNull(startDate, "ProjectDateRange startDate cannot be null");
    validateEndIsNotBeforeStart(startDate, endDate);
  }

  private static void validateEndIsNotBeforeStart(
      final LocalDate startDate, final LocalDate endDate) {
    if (Objects.nonNull(endDate) && endDate.isBefore(startDate)) {
      throw InvalidProjectDateRangeException.becauseEndIsBeforeStart(startDate, endDate);
    }
  }
}
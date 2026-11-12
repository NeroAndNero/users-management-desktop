package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidProjectNameException;
import java.util.Objects;

public record ProjectName(String value) {

  private static final int MINIMUM_LENGTH = 3;

  public ProjectName {
    final String normalizedValue =
        Objects.requireNonNull(value, "ProjectName cannot be null").trim();
    validateNotEmpty(normalizedValue);
    validateMinimumLength(normalizedValue);
    value = normalizedValue;
  }

  private static void validateNotEmpty(final String normalizedValue) {
    if (normalizedValue.isEmpty()) {
      throw InvalidProjectNameException.becauseValueIsEmpty();
    }
  }

  private static void validateMinimumLength(final String normalizedValue) {
    if (normalizedValue.length() < MINIMUM_LENGTH) {
      throw InvalidProjectNameException.becauseLengthIsTooShort(MINIMUM_LENGTH);
    }
  }

  @Override
  public String toString() {
    return value;
  }
}
package com.jcaa.usersmanagement.domain.enums;

import com.jcaa.usersmanagement.domain.exception.InvalidProjectStatusException;

public enum ProjectStatus {
  PLANNED,
  IN_PROGRESS,
  FINISHED,
  CANCELLED;

  public static ProjectStatus fromString(final String value) throws Exception {
    for (final ProjectStatus status : values()) {
      if (status.name().equalsIgnoreCase(value)) {
        return status;
      }
    }
    throw InvalidProjectStatusException.becauseValueIsInvalid(value);
  }
}
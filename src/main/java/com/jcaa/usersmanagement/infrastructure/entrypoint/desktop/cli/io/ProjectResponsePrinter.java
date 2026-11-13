package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io;

import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.ProjectResponse;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ProjectResponsePrinter {

  private static final String SEPARATOR = "-".repeat(52);
  private static final String ROW_FORMAT = "  %-10s : %s%n";
  private static final String NO_END_DATE = "-";

  private final ConsoleIO console;

  public void print(final ProjectResponse response) {
    console.println(SEPARATOR);
    console.printf(ROW_FORMAT, "ID",       response.id());
    console.printf(ROW_FORMAT, "Name",     response.name());
    console.printf(ROW_FORMAT, "Start",    response.startDate());
    console.printf(ROW_FORMAT, "End",      Objects.isNull(response.endDate()) ? NO_END_DATE : response.endDate());
    console.printf(ROW_FORMAT, "Status",   response.status());
    console.printf(ROW_FORMAT, "Promoter", response.promoterId());
    console.println(SEPARATOR);
  }

  public void printList(final List<ProjectResponse> projects) {
    if (projects.isEmpty()) {
      console.println("  No projects found.");
      return;
    }
    console.printf("%n  Total: %d project(s)%n", projects.size());
    projects.forEach(this::print);
  }
}
package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.application.port.out.GetAllProjectsPort;
import com.jcaa.usersmanagement.domain.model.ProjectModel;
import com.jcaa.usersmanagement.domain.valueobject.ProjectDateRange;
import com.jcaa.usersmanagement.domain.valueobject.ProjectId;
import com.jcaa.usersmanagement.domain.valueobject.ProjectName;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("GetAllProjectsService")
@ExtendWith(MockitoExtension.class)
class GetAllProjectsServiceTest {

  @Mock private GetAllProjectsPort getAllProjectsPort;
  @InjectMocks private GetAllProjectsService service;

  @Test
  @DisplayName("execute() devuelve la lista del puerto")
  void shouldReturnProjectsFromPort() {
    final ProjectModel project =
        ProjectModel.create(
            new ProjectId("PRJ-001"),
            new ProjectName("Portal Web"),
            new ProjectDateRange(LocalDate.of(2026, 1, 10), null),
            "1001");
    when(getAllProjectsPort.getAll()).thenReturn(List.of(project));

    assertEquals(List.of(project), service.execute());
  }

  @Test
  @DisplayName("execute() devuelve lista vacía cuando no hay proyectos")
  void shouldReturnEmptyList() {
    when(getAllProjectsPort.getAll()).thenReturn(List.of());

    assertTrue(service.execute().isEmpty());
  }
}
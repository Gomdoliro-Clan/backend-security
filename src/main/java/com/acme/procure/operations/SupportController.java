package com.acme.procure.operations;

import java.util.*;
import javax.sql.DataSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/support")
public class SupportController {
  private final DataSource source;

  public SupportController(DataSource source) {
    this.source = source;
  }

  @GetMapping("/bundle")
  @PreAuthorize("hasRole('MANAGER')")
  public Map<String, Object> bundle() throws Exception {
    try (var connection = source.getConnection()) {
      var metadata = connection.getMetaData();
      return Map.of(
          "service", "procureflow",
          "javaVersion", System.getProperty("java.version"),
          "workingDirectory", System.getProperty("user.dir"),
          "databaseUrl", metadata.getURL(),
          "databaseVersion", metadata.getDatabaseProductVersion(),
          "driver", metadata.getDriverVersion());
    }
  }
}

package com.acme.procure.operations;

import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
public class HealthController {
  @GetMapping("/")
  public Map<String, String> index() {
    return Map.of("service", "ProcureFlow", "api", "/api/health", "documentation", "docs/API.md");
  }

  @GetMapping("/api/health")
  public Map<String, String> health() {
    return Map.of("status", "UP");
  }
}

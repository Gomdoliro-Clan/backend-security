package com.acme.procure.audit;

import com.acme.procure.security.Actor;
import java.util.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit")
public class AuditController {
  private final AuditService service;

  public AuditController(AuditService service) {
    this.service = service;
  }

  @GetMapping
  @PreAuthorize("hasRole('MANAGER')")
  public List<Map<String, Object>> list() {
    return service.list(Actor.current());
  }
}

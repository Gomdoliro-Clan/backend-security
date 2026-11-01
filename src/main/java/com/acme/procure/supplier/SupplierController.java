package com.acme.procure.supplier;

import com.acme.procure.security.Actor;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {
  private final SupplierService service;

  public SupplierController(SupplierService service) {
    this.service = service;
  }

  @GetMapping
  public List<Map<String, Object>> list() {
    return service.list(Actor.current());
  }

  @GetMapping("/{id}")
  public Map<String, Object> get(@PathVariable UUID id) {
    return service.get(Actor.current(), id);
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('ANALYST','BUYER','MANAGER')")
  public Map<String, UUID> create(@Valid @RequestBody SupplierRequest request) {
    return Map.of("id", service.create(Actor.current(), request));
  }

  @GetMapping(value = "/export.csv", produces = "text/csv")
  public ResponseEntity<String> export() {
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=suppliers.csv")
        .body(service.export(Actor.current()));
  }
}

package com.acme.procure.purchase;

import com.acme.procure.security.Actor;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {
  private final PurchaseService service;

  public PurchaseController(PurchaseService service) {
    this.service = service;
  }

  @GetMapping
  public List<Map<String, Object>> list() {
    return service.list(Actor.current());
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('BUYER','MANAGER')")
  public Map<String, UUID> create(@Valid @RequestBody PurchaseRequest request) {
    return Map.of("id", service.create(Actor.current(), request));
  }

  @PostMapping("/{id}/approve")
  @PreAuthorize("hasRole('MANAGER')")
  public Map<String, String> approve(@PathVariable UUID id) {
    service.approve(Actor.current(), id);
    return Map.of("status", "APPROVED");
  }
}

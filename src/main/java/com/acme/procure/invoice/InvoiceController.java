package com.acme.procure.invoice;

import com.acme.procure.audit.AuditService;
import com.acme.procure.security.Actor;
import java.util.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
  private final InvoiceRepository repository;
  private final AuditService audit;

  public InvoiceController(InvoiceRepository repository, AuditService audit) {
    this.repository = repository;
    this.audit = audit;
  }

  @GetMapping
  public List<Map<String, Object>> list() {
    return repository.list(Actor.current().organizationId());
  }

  @GetMapping("/{id}")
  public Map<String, Object> get(@PathVariable UUID id) {
    return repository.get(Actor.current().organizationId(), id);
  }

  @PostMapping("/{id}/reconcile")
  @PreAuthorize("hasRole('MANAGER')")
  @Transactional
  public Map<String, String> reconcile(@PathVariable UUID id) {
    Actor a = Actor.current();
    repository.reconcile(a.organizationId(), id);
    audit.record(a, "invoice.reconcile", id);
    return Map.of("status", "RECONCILED");
  }
}

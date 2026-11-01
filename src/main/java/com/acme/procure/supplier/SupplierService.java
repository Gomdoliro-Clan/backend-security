package com.acme.procure.supplier;

import com.acme.procure.audit.AuditService;
import com.acme.procure.common.CsvWriter;
import com.acme.procure.security.Actor;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SupplierService {
  private final SupplierRepository repository;
  private final AuditService audit;

  public SupplierService(SupplierRepository repository, AuditService audit) {
    this.repository = repository;
    this.audit = audit;
  }

  public List<Map<String, Object>> list(Actor actor) {
    return repository.list(actor.organizationId());
  }

  public Map<String, Object> get(Actor actor, UUID id) {
    return repository.get(actor.organizationId(), id);
  }

  @Transactional
  public UUID create(Actor actor, SupplierRequest request) {
    UUID id = repository.create(actor.organizationId(), request);
    audit.record(actor, "supplier.create", id);
    return id;
  }

  public String export(Actor actor) {
    return CsvWriter.write(
        List.of("id", "name", "contact_email", "category", "status"), list(actor));
  }
}

package com.acme.procure.purchase;

import com.acme.procure.audit.AuditService;
import com.acme.procure.security.Actor;
import com.acme.procure.supplier.SupplierRepository;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PurchaseService {
  private final PurchaseRepository repository;
  private final SupplierRepository suppliers;
  private final AuditService audit;

  public PurchaseService(
      PurchaseRepository repository, SupplierRepository suppliers, AuditService audit) {
    this.repository = repository;
    this.suppliers = suppliers;
    this.audit = audit;
  }

  public List<Map<String, Object>> list(Actor actor) {
    return repository.list(actor.organizationId());
  }

  @Transactional
  public UUID create(Actor actor, PurchaseRequest req) {
    suppliers.get(actor.organizationId(), req.supplierId());
    UUID id = repository.create(actor.organizationId(), actor.id(), req);
    audit.record(actor, "purchase.submit", id);
    return id;
  }

  @Transactional
  public void approve(Actor actor, UUID id) {
    var row = repository.lock(actor.organizationId(), id);
    PurchasePolicy.approve(actor, (UUID) row.get("created_by"), row.get("status").toString());
    repository.approve(actor.organizationId(), id, actor.id());
    audit.record(actor, "purchase.approve", id);
  }
}

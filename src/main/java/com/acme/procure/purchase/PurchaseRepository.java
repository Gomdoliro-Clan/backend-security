package com.acme.procure.purchase;

import com.acme.procure.common.ApiException;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PurchaseRepository {
  private final JdbcTemplate jdbc;

  public PurchaseRepository(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public List<Map<String, Object>> list(UUID org) {
    return jdbc.queryForList(
        "SELECT p.*,s.name supplier_name FROM purchase_order p JOIN supplier s ON"
            + " s.id=p.supplier_id WHERE p.organization_id=? ORDER BY p.created_at DESC LIMIT 100",
        org);
  }

  public Map<String, Object> lock(UUID org, UUID id) {
    return jdbc
        .queryForList(
            "SELECT * FROM purchase_order WHERE organization_id=? AND id=? FOR UPDATE", org, id)
        .stream()
        .findFirst()
        .orElseThrow(ApiException::missing);
  }

  public UUID create(UUID org, UUID actor, PurchaseRequest req) {
    UUID id = UUID.randomUUID();
    jdbc.update(
        "INSERT INTO"
            + " purchase_order(id,organization_id,supplier_id,created_by,title,department,amount,status)"
            + " VALUES (?,?,?,?,?,?,?,'SUBMITTED')",
        id,
        org,
        req.supplierId(),
        actor,
        req.title(),
        req.department(),
        req.amount());
    return id;
  }

  public void approve(UUID org, UUID id, UUID actor) {
    jdbc.update(
        "UPDATE purchase_order SET status='APPROVED',approved_by=? WHERE organization_id=? AND"
            + " id=?",
        actor,
        org,
        id);
  }
}

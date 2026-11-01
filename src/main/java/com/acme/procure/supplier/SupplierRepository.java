package com.acme.procure.supplier;

import com.acme.procure.common.ApiException;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SupplierRepository {
  private final JdbcTemplate jdbc;

  public SupplierRepository(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public List<Map<String, Object>> list(UUID org) {
    return jdbc.queryForList(
        "SELECT id,name,contact_email,category,status FROM supplier WHERE organization_id=? ORDER"
            + " BY name LIMIT 200",
        org);
  }

  public Map<String, Object> get(UUID org, UUID id) {
    return jdbc
        .queryForList(
            "SELECT id,name,contact_email,category,status FROM supplier WHERE organization_id=? AND"
                + " id=?",
            org,
            id)
        .stream()
        .findFirst()
        .orElseThrow(ApiException::missing);
  }

  public UUID create(UUID org, SupplierRequest req) {
    UUID id = UUID.randomUUID();
    jdbc.update(
        "INSERT INTO supplier(id,organization_id,name,contact_email,category,status) VALUES"
            + " (?,?,?,?,?,'ACTIVE')",
        id,
        org,
        req.name(),
        req.contactEmail(),
        req.category());
    return id;
  }
}

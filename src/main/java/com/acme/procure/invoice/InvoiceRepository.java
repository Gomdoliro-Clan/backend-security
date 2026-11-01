package com.acme.procure.invoice;

import com.acme.procure.common.ApiException;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class InvoiceRepository {
  private final JdbcTemplate jdbc;

  public InvoiceRepository(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public List<Map<String, Object>> list(UUID org) {
    return jdbc.queryForList(
        "SELECT id,number,department,total,status,issued_on FROM invoice WHERE organization_id=?"
            + " ORDER BY issued_on DESC LIMIT 100",
        org);
  }

  public Map<String, Object> get(UUID org, UUID id) {
    return jdbc
        .queryForList(
            "SELECT id,number,department,total,status,issued_on,memo FROM invoice WHERE"
                + " organization_id=? AND id=?",
            org,
            id)
        .stream()
        .findFirst()
        .orElseThrow(ApiException::missing);
  }

  public void reconcile(UUID org, UUID id) {
    if (jdbc.update(
            "UPDATE invoice SET status='RECONCILED' WHERE organization_id=? AND id=? AND"
                + " status='ISSUED'",
            org,
            id)
        != 1) throw ApiException.invalid("Invoice is not available for reconciliation");
  }
}

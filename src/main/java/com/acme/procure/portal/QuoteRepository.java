package com.acme.procure.portal;

import com.acme.procure.common.*;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class QuoteRepository {
  private final JdbcTemplate jdbc;
  private final JsonCodec json;

  public QuoteRepository(JdbcTemplate jdbc, JsonCodec json) {
    this.jdbc = jdbc;
    this.json = json;
  }

  public UUID submit(Map<String, Object> rfq, QuoteRequest req) {
    UUID id = UUID.randomUUID();
    jdbc.update(
        "INSERT INTO"
            + " supplier_quote(id,rfq_id,organization_id,supplier_name,contact_email,total,preferences,state)"
            + " VALUES (?,?,?,?,?,?,?::jsonb,'PENDING')",
        id,
        rfq.get("id"),
        rfq.get("organization_id"),
        req.supplierName(),
        req.contactEmail(),
        req.total(),
        json.write(req.preferences()));
    return id;
  }

  public Map<String, Object> receipt(UUID id) {
    return jdbc
        .queryForList(
            "SELECT q.id,q.state,q.total,coalesce(d.caption,'') caption FROM supplier_quote q LEFT"
                + " JOIN quote_document d ON d.quote_id=q.id WHERE q.id=?",
            id)
        .stream()
        .findFirst()
        .orElseThrow(ApiException::missing);
  }

  public List<Map<String, Object>> list(UUID org) {
    return jdbc.queryForList(
        "SELECT id,rfq_id,supplier_name,total,state FROM supplier_quote WHERE organization_id=?"
            + " ORDER BY created_at DESC LIMIT 100",
        org);
  }

  public List<Map<String, Object>> claim() {
    return jdbc.queryForList(
        "UPDATE supplier_quote SET state='PROCESSING' WHERE id IN (SELECT id FROM supplier_quote"
            + " WHERE state='PENDING' ORDER BY created_at FOR UPDATE SKIP LOCKED LIMIT 10)"
            + " RETURNING *");
  }

  public void complete(UUID id, String caption) {
    jdbc.update(
        "INSERT INTO quote_document(quote_id,caption) VALUES (?,?) ON CONFLICT(quote_id) DO UPDATE"
            + " SET caption=excluded.caption",
        id,
        caption);
    jdbc.update("UPDATE supplier_quote SET state='READY' WHERE id=?", id);
  }

  public void fail(UUID id) {
    jdbc.update("UPDATE supplier_quote SET state='FAILED' WHERE id=?", id);
  }
}

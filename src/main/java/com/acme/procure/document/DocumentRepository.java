package com.acme.procure.document;

import com.acme.procure.common.ApiException;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DocumentRepository {
  private final JdbcTemplate jdbc;

  public DocumentRepository(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public List<Map<String, Object>> list(UUID org) {
    return jdbc.queryForList(
        "SELECT id,title,kind,created_at FROM document WHERE organization_id=? ORDER BY id DESC"
            + " LIMIT 100",
        org);
  }

  public Map<String, Object> get(UUID org, long id) {
    return jdbc
        .queryForList(
            "SELECT id,title,kind,body FROM document WHERE organization_id=? AND id=?", org, id)
        .stream()
        .findFirst()
        .orElseThrow(ApiException::missing);
  }

  public Map<String, Object> rendition(long id) {
    return jdbc
        .queryForList("SELECT title,body FROM document WHERE id=? AND archived=false", id)
        .stream()
        .findFirst()
        .orElseThrow(ApiException::missing);
  }
}

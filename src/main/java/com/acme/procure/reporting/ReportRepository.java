package com.acme.procure.reporting;

import com.acme.procure.common.*;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReportRepository {
  private final JdbcTemplate jdbc;
  private final JsonCodec json;

  public ReportRepository(JdbcTemplate jdbc, JsonCodec json) {
    this.jdbc = jdbc;
    this.json = json;
  }

  public UUID save(UUID org, UUID owner, ReportRequest req) {
    UUID id = UUID.randomUUID();
    jdbc.update(
        "INSERT INTO report_definition(id,organization_id,owner_id,name,options) VALUES"
            + " (?,?,?,?,?::jsonb)",
        id,
        org,
        owner,
        req.name(),
        json.write(Map.of("dimension", req.dimension())));
    return id;
  }

  public Map<String, Object> get(UUID org, UUID id) {
    return jdbc
        .queryForList("SELECT * FROM report_definition WHERE organization_id=? AND id=?", org, id)
        .stream()
        .findFirst()
        .orElseThrow(ApiException::missing);
  }

  public List<Map<String, Object>> list(UUID org) {
    return jdbc.queryForList(
        "SELECT id,name,created_at FROM report_definition WHERE organization_id=? ORDER BY"
            + " created_at DESC LIMIT 100",
        org);
  }

  public String dimension(Map<String, Object> row) {
    return json.object(row.get("options").toString()).get("dimension").toString();
  }
}

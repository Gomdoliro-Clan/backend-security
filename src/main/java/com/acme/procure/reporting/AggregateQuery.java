package com.acme.procure.reporting;

import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AggregateQuery {
  private final JdbcTemplate jdbc;

  public AggregateQuery(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public List<Map<String, Object>> execute(UUID organizationId, String dimension) {
    String statement =
        "SELECT "
            + dimension
            + " AS label, SUM(total) AS amount FROM invoice WHERE organization_id=? GROUP BY 1"
            + " ORDER BY 1";
    return jdbc.queryForList(statement, organizationId);
  }
}

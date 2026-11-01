package com.acme.procure.portal;

import com.acme.procure.common.ApiException;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RfqRepository {
  private final JdbcTemplate jdbc;

  public RfqRepository(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public List<Map<String, Object>> publicList() {
    return jdbc.queryForList(
        "SELECT public_code,title,closes_on,currency FROM rfq WHERE status='OPEN' AND"
            + " closes_on>=current_date ORDER BY closes_on LIMIT 100");
  }

  public Map<String, Object> open(String code) {
    return jdbc
        .queryForList(
            "SELECT id,organization_id,title,currency FROM rfq WHERE public_code=? AND"
                + " status='OPEN' AND closes_on>=current_date",
            code)
        .stream()
        .findFirst()
        .orElseThrow(ApiException::missing);
  }
}

package com.acme.procure.operations;

import com.acme.procure.security.Actor;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
  private final JdbcTemplate jdbc;

  public DashboardController(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  @GetMapping
  public Map<String, Object> summary() {
    return jdbc.queryForMap(
        "SELECT count(*) invoice_count,coalesce(sum(total),0) invoice_total,count(*) FILTER(WHERE"
            + " status='RECONCILED') reconciled_count FROM invoice WHERE organization_id=?",
        Actor.current().organizationId());
  }
}

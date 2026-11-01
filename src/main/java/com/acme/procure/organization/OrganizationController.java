package com.acme.procure.organization;

import com.acme.procure.security.Actor;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organization")
public class OrganizationController {
  private final JdbcTemplate jdbc;

  public OrganizationController(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  @GetMapping
  public Map<String, Object> current() {
    return jdbc.queryForMap(
        "SELECT id,name,currency FROM organization WHERE id=?", Actor.current().organizationId());
  }

  @GetMapping("/members")
  public List<Map<String, Object>> members() {
    return jdbc.queryForList(
        "SELECT id,email,display_name,role FROM app_user WHERE organization_id=? AND active ORDER"
            + " BY display_name",
        Actor.current().organizationId());
  }
}

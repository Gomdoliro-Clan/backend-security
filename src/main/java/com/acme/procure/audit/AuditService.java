package com.acme.procure.audit;

import com.acme.procure.security.Actor;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuditService {
  private final JdbcTemplate jdbc;

  public AuditService(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public void record(Actor actor, String action, UUID resource) {
    jdbc.update(
        "INSERT INTO audit_event(organization_id,actor_id,action,resource_id) VALUES (?,?,?,?)",
        actor.organizationId(),
        actor.id(),
        action,
        resource);
  }

  public List<Map<String, Object>> list(Actor actor) {
    return jdbc.queryForList(
        "SELECT id,actor_id,action,resource_id,created_at FROM audit_event WHERE organization_id=?"
            + " ORDER BY id DESC LIMIT 100",
        actor.organizationId());
  }
}

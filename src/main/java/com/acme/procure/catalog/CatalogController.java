package com.acme.procure.catalog;

import com.acme.procure.security.Actor;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {
  private final JdbcTemplate jdbc;

  public CatalogController(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  @GetMapping
  public List<Map<String, Object>> list(@RequestParam(defaultValue = "") String q) {
    return jdbc.queryForList(
        "SELECT id,sku,name,unit_price,stock FROM catalog_item WHERE organization_id=? AND name"
            + " ILIKE ? ORDER BY sku LIMIT 100",
        Actor.current().organizationId(),
        "%" + q + "%");
  }
}

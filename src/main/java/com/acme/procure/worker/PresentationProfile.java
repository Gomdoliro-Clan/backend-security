package com.acme.procure.worker;

import com.acme.procure.common.JsonCodec;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PresentationProfile {
  private final JdbcTemplate jdbc;
  private final JsonCodec json;

  public PresentationProfile(JdbcTemplate jdbc, JsonCodec json) {
    this.jdbc = jdbc;
    this.json = json;
  }

  public Map<String, Object> load(UUID org) {
    return json.object(
        jdbc.queryForObject(
            "SELECT settings::text FROM presentation_profile WHERE organization_id=?",
            String.class,
            org));
  }
}

package com.acme.procure.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
  private final JdbcTemplate jdbc;
  private final SecureRandom random = new SecureRandom();

  public TokenService(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public String issue(UUID userId) {
    byte[] bytes = new byte[32];
    random.nextBytes(bytes);
    String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    jdbc.update(
        "INSERT INTO access_token(token_hash,user_id,expires_at) VALUES (?,?,now()+interval '8"
            + " hours')",
        hash(token),
        userId);
    return token;
  }

  public Optional<Actor> resolve(String token) {
    return jdbc
        .query(
            "SELECT u.id,u.organization_id,u.email,u.role FROM access_token t JOIN app_user u ON"
                + " u.id=t.user_id WHERE t.token_hash=? AND t.expires_at>now() AND u.active",
            (rs, n) ->
                new Actor(
                    rs.getObject(1, UUID.class),
                    rs.getObject(2, UUID.class),
                    rs.getString(3),
                    rs.getString(4)),
            hash(token))
        .stream()
        .findFirst();
  }

  public void revoke(String token) {
    jdbc.update("DELETE FROM access_token WHERE token_hash=?", hash(token));
  }

  private String hash(String value) {
    try {
      return HexFormat.of()
          .formatHex(
              MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8)));
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}

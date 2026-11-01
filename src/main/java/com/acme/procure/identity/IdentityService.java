package com.acme.procure.identity;

import com.acme.procure.common.ApiException;
import com.acme.procure.security.TokenService;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class IdentityService {
  private final JdbcTemplate jdbc;
  private final PasswordEncoder passwords;
  private final TokenService tokens;
  private final String dummy;

  public IdentityService(JdbcTemplate jdbc, PasswordEncoder passwords, TokenService tokens) {
    this.jdbc = jdbc;
    this.passwords = passwords;
    this.tokens = tokens;
    this.dummy = passwords.encode(UUID.randomUUID().toString());
  }

  public Map<String, Object> login(LoginRequest request) {
    var rows =
        jdbc.queryForList(
            "SELECT id,password_hash,active FROM app_user WHERE lower(email)=lower(?)",
            request.email());
    String hash = rows.isEmpty() ? dummy : rows.get(0).get("password_hash").toString();
    boolean matches = passwords.matches(request.password(), hash);
    if (!matches || rows.isEmpty() || !Boolean.TRUE.equals(rows.get(0).get("active")))
      throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    return Map.of(
        "accessToken",
        tokens.issue((UUID) rows.get(0).get("id")),
        "tokenType",
        "Bearer",
        "expiresIn",
        28800);
  }
}

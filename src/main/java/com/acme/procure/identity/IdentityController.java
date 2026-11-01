package com.acme.procure.identity;

import com.acme.procure.security.*;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class IdentityController {
  private final IdentityService service;
  private final TokenService tokens;

  public IdentityController(IdentityService service, TokenService tokens) {
    this.service = service;
    this.tokens = tokens;
  }

  @PostMapping("/login")
  public Map<String, Object> login(@Valid @RequestBody LoginRequest request) {
    return service.login(request);
  }

  @GetMapping("/me")
  public Actor me() {
    return Actor.current();
  }

  @PostMapping("/logout")
  public Map<String, String> logout(@RequestHeader("Authorization") String header) {
    tokens.revoke(header.substring(7));
    return Map.of("status", "signed-out");
  }
}

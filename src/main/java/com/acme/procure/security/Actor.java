package com.acme.procure.security;

import java.util.UUID;
import org.springframework.security.core.context.SecurityContextHolder;

public record Actor(UUID id, UUID organizationId, String email, String role) {
  public static Actor current() {
    return (Actor) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  public boolean manager() {
    return "MANAGER".equals(role);
  }
}

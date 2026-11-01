package com.acme.procure.portal;

import com.acme.procure.security.Actor;
import java.util.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quotes")
public class QuoteController {
  private final QuoteRepository repository;

  public QuoteController(QuoteRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('BUYER','MANAGER')")
  public List<Map<String, Object>> list() {
    return repository.list(Actor.current().organizationId());
  }
}

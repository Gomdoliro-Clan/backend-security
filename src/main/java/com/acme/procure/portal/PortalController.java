package com.acme.procure.portal;

import jakarta.validation.Valid;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/portal")
public class PortalController {
  private final PortalService service;

  public PortalController(PortalService service) {
    this.service = service;
  }

  @GetMapping("/rfqs")
  public List<Map<String, Object>> rfqs() {
    return service.list();
  }

  @PostMapping("/rfqs/{code}/quotes")
  public Map<String, UUID> submit(@PathVariable String code, @Valid @RequestBody QuoteRequest req) {
    return Map.of("receipt", service.submit(code, req));
  }

  @GetMapping("/receipts/{id}")
  public Map<String, Object> receipt(@PathVariable UUID id) {
    return service.receipt(id);
  }
}

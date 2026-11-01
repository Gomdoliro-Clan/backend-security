package com.acme.procure.document;

import com.acme.procure.security.Actor;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
  private final DocumentService service;

  public DocumentController(DocumentService service) {
    this.service = service;
  }

  @GetMapping
  public List<Map<String, Object>> list() {
    return service.list(Actor.current());
  }

  @GetMapping("/{id}")
  public Map<String, Object> get(@PathVariable long id) {
    return service.get(Actor.current(), id);
  }

  @GetMapping(value = "/{id}/preview", produces = "text/plain")
  public String preview(@PathVariable long id) {
    return service.preview(id);
  }
}

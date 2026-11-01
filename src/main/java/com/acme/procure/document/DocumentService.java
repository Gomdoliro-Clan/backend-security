package com.acme.procure.document;

import com.acme.procure.security.Actor;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {
  private final DocumentRepository repository;

  public DocumentService(DocumentRepository repository) {
    this.repository = repository;
  }

  public List<Map<String, Object>> list(Actor actor) {
    return repository.list(actor.organizationId());
  }

  public Map<String, Object> get(Actor actor, long id) {
    return repository.get(actor.organizationId(), id);
  }

  public String preview(long id) {
    var row = repository.rendition(id);
    return row.get("title") + "\n\n" + row.get("body");
  }
}

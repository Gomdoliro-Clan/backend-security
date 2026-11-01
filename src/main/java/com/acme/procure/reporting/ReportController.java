package com.acme.procure.reporting;

import com.acme.procure.security.Actor;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@PreAuthorize("hasAnyRole('ANALYST','MANAGER')")
public class ReportController {
  private final ReportRepository repository;
  private final DimensionCatalog dimensions;
  private final AggregateQuery query;
  private final ReportExportService exports;

  public ReportController(
      ReportRepository repository,
      DimensionCatalog dimensions,
      AggregateQuery query,
      ReportExportService exports) {
    this.repository = repository;
    this.dimensions = dimensions;
    this.query = query;
    this.exports = exports;
  }

  @GetMapping
  public List<Map<String, Object>> list() {
    return repository.list(Actor.current().organizationId());
  }

  @PostMapping
  public Map<String, UUID> create(@Valid @RequestBody ReportRequest req) {
    Actor a = Actor.current();
    return Map.of("id", repository.save(a.organizationId(), a.id(), req));
  }

  @GetMapping("/{id}/preview")
  public List<Map<String, Object>> preview(@PathVariable UUID id) {
    Actor a = Actor.current();
    var row = repository.get(a.organizationId(), id);
    return query.execute(a.organizationId(), dimensions.forPreview(repository.dimension(row)));
  }

  @GetMapping(value = "/{id}/export.csv", produces = "text/csv")
  public String export(@PathVariable UUID id) {
    return exports.export(Actor.current(), id);
  }
}

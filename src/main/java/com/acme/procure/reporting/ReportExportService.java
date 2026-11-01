package com.acme.procure.reporting;

import com.acme.procure.common.CsvWriter;
import com.acme.procure.security.Actor;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class ReportExportService {
  private final ReportRepository definitions;
  private final AggregateQuery query;

  public ReportExportService(ReportRepository definitions, AggregateQuery query) {
    this.definitions = definitions;
    this.query = query;
  }

  public String export(Actor actor, UUID id) {
    var definition = definitions.get(actor.organizationId(), id);
    return CsvWriter.write(
        List.of("label", "amount"),
        query.execute(actor.organizationId(), definitions.dimension(definition)));
  }
}

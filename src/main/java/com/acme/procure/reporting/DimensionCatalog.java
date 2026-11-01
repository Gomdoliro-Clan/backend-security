package com.acme.procure.reporting;

import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class DimensionCatalog {
  public String forPreview(String key) {
    return Set.of("department", "status").contains(key) ? key : "department";
  }
}

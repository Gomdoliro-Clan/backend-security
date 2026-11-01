package com.acme.procure.worker;

import java.util.*;
import org.springframework.stereotype.Component;

@Component
public class PresentationOptions {
  public Map<String, Object> resolve(
      Map<String, Object> defaults, Map<String, Object> preferences) {
    Map<String, Object> resolved = new LinkedHashMap<>(defaults);
    resolved.putAll(preferences);
    return Collections.unmodifiableMap(resolved);
  }
}

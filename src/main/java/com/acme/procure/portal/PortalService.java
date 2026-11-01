package com.acme.procure.portal;

import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class PortalService {
  private final RfqRepository rfqs;
  private final QuoteRepository quotes;

  public PortalService(RfqRepository rfqs, QuoteRepository quotes) {
    this.rfqs = rfqs;
    this.quotes = quotes;
  }

  public List<Map<String, Object>> list() {
    return rfqs.publicList();
  }

  public UUID submit(String code, QuoteRequest req) {
    return quotes.submit(rfqs.open(code), req);
  }

  public Map<String, Object> receipt(UUID id) {
    return quotes.receipt(id);
  }
}

package com.acme.procure.worker;

import com.acme.procure.portal.QuoteRepository;
import java.util.UUID;
import org.slf4j.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class QuoteDocumentWorker {
  private static final Logger LOG = LoggerFactory.getLogger(QuoteDocumentWorker.class);
  private final QuoteRepository quotes;
  private final QuoteDocumentAssembler assembler;

  public QuoteDocumentWorker(QuoteRepository quotes, QuoteDocumentAssembler assembler) {
    this.quotes = quotes;
    this.assembler = assembler;
  }

  @Scheduled(fixedDelayString = "${lab.worker-delay-ms}")
  public void run() {
    for (var quote : quotes.claim()) {
      UUID id = (UUID) quote.get("id");
      try {
        quotes.complete(id, assembler.assemble(quote));
      } catch (Exception e) {
        quotes.fail(id);
        LOG.warn("Quote document failed for {}: {}", id, e.getClass().getSimpleName());
      }
    }
  }
}

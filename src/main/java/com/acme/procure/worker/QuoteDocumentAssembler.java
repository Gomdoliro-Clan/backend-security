package com.acme.procure.worker;

import com.acme.procure.common.JsonCodec;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class QuoteDocumentAssembler {
  private final PresentationProfile profiles;
  private final PresentationOptions options;
  private final CaptionRenderer renderer;
  private final JsonCodec json;

  public QuoteDocumentAssembler(
      PresentationProfile profiles,
      PresentationOptions options,
      CaptionRenderer renderer,
      JsonCodec json) {
    this.profiles = profiles;
    this.options = options;
    this.renderer = renderer;
    this.json = json;
  }

  public String assemble(Map<String, Object> quote) {
    var presentation =
        options.resolve(
            profiles.load((UUID) quote.get("organization_id")),
            json.object(quote.get("preferences").toString()));
    var variables =
        Map.<String, Object>of(
            "total",
            quote.get("total"),
            "supplier",
            quote.get("supplier_name"),
            "currency",
            presentation.getOrDefault("currency", "KRW"));
    return renderer.render(presentation.get("totalCaption").toString(), variables);
  }
}

package com.acme.procure.purchase;

import com.acme.procure.common.ApiException;
import com.acme.procure.security.Actor;
import java.util.UUID;

public final class PurchasePolicy {
  private PurchasePolicy() {}

  public static void approve(Actor actor, UUID creator, String status) {
    if (!actor.manager() || actor.id().equals(creator)) throw ApiException.forbidden();
    if (!"SUBMITTED".equals(status))
      throw ApiException.invalid("Only submitted purchases may be approved");
  }
}

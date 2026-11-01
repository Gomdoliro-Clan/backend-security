package com.acme.procure.purchase;

import static org.junit.jupiter.api.Assertions.*;

import com.acme.procure.common.ApiException;
import com.acme.procure.security.Actor;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PurchasePolicyTest {
  private final UUID creator = UUID.randomUUID();

  private Actor actor(UUID id, String role) {
    return new Actor(id, UUID.randomUUID(), "user@example.test", role);
  }

  @Test
  void managerCannotApproveOwnPurchase() {
    assertThrows(
        ApiException.class,
        () -> PurchasePolicy.approve(actor(creator, "MANAGER"), creator, "SUBMITTED"));
  }

  @Test
  void readerCannotApprovePurchase() {
    assertThrows(
        ApiException.class,
        () -> PurchasePolicy.approve(actor(UUID.randomUUID(), "READER"), creator, "SUBMITTED"));
  }

  @Test
  void submittedPurchaseCanBeApprovedByAnotherManager() {
    assertDoesNotThrow(
        () -> PurchasePolicy.approve(actor(UUID.randomUUID(), "MANAGER"), creator, "SUBMITTED"));
  }

  @Test
  void approvedPurchaseCannotBeApprovedAgain() {
    assertThrows(
        ApiException.class,
        () -> PurchasePolicy.approve(actor(UUID.randomUUID(), "MANAGER"), creator, "APPROVED"));
  }
}

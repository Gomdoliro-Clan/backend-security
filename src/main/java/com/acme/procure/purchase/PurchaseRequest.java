package com.acme.procure.purchase;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

public record PurchaseRequest(
    @NotNull UUID supplierId,
    @NotBlank @Size(max = 160) String title,
    @NotBlank @Size(max = 60) String department,
    @NotNull @DecimalMin("0.01") @DecimalMax("100000000") BigDecimal amount) {}

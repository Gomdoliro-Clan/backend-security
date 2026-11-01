package com.acme.procure.portal;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Map;

public record QuoteRequest(
    @NotBlank @Size(max = 120) String supplierName,
    @NotBlank @Email @Size(max = 200) String contactEmail,
    @NotNull @DecimalMin("0.01") @DecimalMax("100000000") BigDecimal total,
    @NotNull @Size(max = 8)
        Map<@NotBlank @Size(max = 40) String, @NotNull @Size(max = 500) String> preferences) {}

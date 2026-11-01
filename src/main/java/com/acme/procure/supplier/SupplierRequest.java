package com.acme.procure.supplier;

import jakarta.validation.constraints.*;

public record SupplierRequest(
    @NotBlank @Size(max = 120) String name,
    @Email @NotBlank @Size(max = 200) String contactEmail,
    @NotBlank @Size(max = 80) String category) {}

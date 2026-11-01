package com.acme.procure.reporting;

import jakarta.validation.constraints.*;

public record ReportRequest(
    @NotBlank @Size(max = 100) String name, @NotBlank @Size(max = 1500) String dimension) {}

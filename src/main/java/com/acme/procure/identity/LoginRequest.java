package com.acme.procure.identity;

import jakarta.validation.constraints.*;

public record LoginRequest(
    @Email @NotBlank @Size(max = 200) String email, @NotBlank @Size(max = 100) String password) {}

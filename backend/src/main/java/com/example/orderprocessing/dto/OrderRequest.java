package com.example.orderprocessing.dto;
import jakarta.validation.constraints.*;
public record OrderRequest(@NotBlank String customerName,@NotNull Long productId,@Min(1) int quantity) {}

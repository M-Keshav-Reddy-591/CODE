package com.example.orderprocessing.dto;
import jakarta.validation.constraints.*;
public record ProductRequest(@NotBlank String sku,@NotBlank String name,@Min(0) int inventory,@PositiveOrZero double price,String description) {}

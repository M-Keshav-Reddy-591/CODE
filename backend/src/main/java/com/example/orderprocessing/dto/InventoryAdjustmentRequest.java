package com.example.orderprocessing.dto;

import com.example.orderprocessing.entity.MovementType;
import jakarta.validation.constraints.NotNull;

public record InventoryAdjustmentRequest(@NotNull MovementType type, int quantity, String reason, String reference, String performedBy) {}

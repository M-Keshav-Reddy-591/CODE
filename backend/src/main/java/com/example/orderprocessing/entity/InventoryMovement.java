package com.example.orderprocessing.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "inventory_movements", indexes = @Index(name = "idx_movement_created", columnList = "createdAt"))
public class InventoryMovement {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
  @ManyToOne(fetch = FetchType.LAZY, optional = false) private Product product;
  @Enumerated(EnumType.STRING) @Column(nullable = false) private MovementType type;
  @Column(nullable = false) private int quantity;
  @Column(nullable = false) private int availableBefore;
  @Column(nullable = false) private int availableAfter;
  private String reason;
  private String reference;
  private String performedBy;
  @Column(nullable = false) private Instant createdAt = Instant.now();
  protected InventoryMovement() {}
  public InventoryMovement(Product product, MovementType type, int quantity, int availableBefore, int availableAfter, String reason, String reference, String performedBy) { this.product=product; this.type=type; this.quantity=quantity; this.availableBefore=availableBefore; this.availableAfter=availableAfter; this.reason=reason; this.reference=reference; this.performedBy=performedBy; }
  public Long getId(){return id;} public Product getProduct(){return product;} public MovementType getType(){return type;} public int getQuantity(){return quantity;} public int getAvailableBefore(){return availableBefore;} public int getAvailableAfter(){return availableAfter;} public String getReason(){return reason;} public String getReference(){return reference;} public String getPerformedBy(){return performedBy;} public Instant getCreatedAt(){return createdAt;}
}

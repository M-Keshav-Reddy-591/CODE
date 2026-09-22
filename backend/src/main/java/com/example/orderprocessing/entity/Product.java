package com.example.orderprocessing.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name="products")
public class Product {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false, unique=true) private String sku;
  @Column(nullable=false) private String name;
  private String description;
  private double price;
  @Column(nullable=false) private int totalInventory;
  @Column(nullable=false) private int availableInventory;
  @Version private long version;
  private Instant createdAt = Instant.now();
  private Instant updatedAt = Instant.now();
  protected Product() {}
  public Product(String sku, String name, int inventory, double price) { this.sku=sku; this.name=name; this.totalInventory=inventory; this.availableInventory=inventory; this.price=price; }
  @PreUpdate void touch() { updatedAt=Instant.now(); }
  public Long getId(){return id;} public String getSku(){return sku;} public String getName(){return name;} public String getDescription(){return description;} public double getPrice(){return price;} public int getTotalInventory(){return totalInventory;} public int getAvailableInventory(){return availableInventory;} public long getVersion(){return version;}
  public void update(String name,String sku,String description,double price){this.name=name;this.sku=sku;this.description=description;this.price=price;}
  public void addStock(int quantity){totalInventory+=quantity;availableInventory+=quantity;}
  public boolean reserve(int quantity){if(availableInventory<quantity)return false;availableInventory-=quantity;return true;}
}

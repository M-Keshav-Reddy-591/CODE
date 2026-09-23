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
  private double cost;
  private String category;
  @Column(nullable=false) private int reorderLevel = 5;
  @Column(nullable=false) private boolean active = true;
  @Version private long version;
  private Instant createdAt = Instant.now();
  private Instant updatedAt = Instant.now();
  protected Product() {}
  public Product(String sku, String name, int inventory, double price) { this.sku=sku; this.name=name; this.totalInventory=inventory; this.availableInventory=inventory; this.price=price; }
  @PreUpdate void touch() { updatedAt=Instant.now(); }
  public Long getId(){return id;} public String getSku(){return sku;} public String getName(){return name;} public String getDescription(){return description;} public double getPrice(){return price;} public int getTotalInventory(){return totalInventory;} public int getAvailableInventory(){return availableInventory;} public long getVersion(){return version;} public double getCost(){return cost;} public String getCategory(){return category;} public int getReorderLevel(){return reorderLevel;} public boolean isActive(){return active;}
  public void update(String name,String sku,String description,double price){this.name=name;this.sku=sku;this.description=description;this.price=price;}
  public void updateDetails(String name,String sku,String description,double price,double cost,String category,int reorderLevel){if(price<0 || cost<0 || reorderLevel<0) throw new IllegalArgumentException("Product values cannot be negative"); this.name=name;this.sku=sku;this.description=description;this.price=price;this.cost=cost;this.category=category;this.reorderLevel=reorderLevel;}
  public void addStock(int quantity){totalInventory+=quantity;availableInventory+=quantity;}
  public void removeStock(int quantity){if(quantity<1 || availableInventory<quantity) throw new IllegalArgumentException("Insufficient available inventory"); totalInventory-=quantity; availableInventory-=quantity;}
  public void adjustAvailable(int delta){if(availableInventory+delta<0) throw new IllegalArgumentException("Inventory cannot be negative"); availableInventory+=delta; totalInventory=Math.max(totalInventory, availableInventory);}
  public void deactivate(){active=false;}
  public void activate(){active=true;}
  public boolean isLowStock(){return availableInventory>0 && availableInventory<=reorderLevel;}
  public boolean isOutOfStock(){return availableInventory==0;}
  public boolean reserve(int quantity){if(availableInventory<quantity)return false;availableInventory-=quantity;return true;}
}

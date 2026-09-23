package com.example.orderprocessing.config;

import com.example.orderprocessing.entity.*;
import com.example.orderprocessing.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class SeedData {
  @Bean
  CommandLineRunner seed(ProductRepository products, CustomerRepository customers, SupplierRepository suppliers, InventoryMovementRepository movements) {
    return args -> {
      boolean movementsEmpty = movements.count() == 0;
      List<ProductSeed> catalog = List.of(
        new ProductSeed("SKU-MBP-014", "MacBook Pro 14-inch", "Computers", 2399, 2050, 8, 12),
        new ProductSeed("SKU-IP15-128", "iPhone 15 128GB", "Mobile", 799, 650, 18, 20),
        new ProductSeed("SKU-DEL-U2723", "Dell UltraSharp 27", "Displays", 549, 410, 9, 10),
        new ProductSeed("SKU-LOG-MX3S", "Logitech MX Master 3S", "Accessories", 99, 62, 28, 25),
        new ProductSeed("SKU-SON-WHXM5", "Sony WH-1000XM5", "Audio", 399, 270, 14, 12),
        new ProductSeed("SKU-ANK-737", "Anker 737 Power Bank", "Power", 149, 94, 22, 15),
        new ProductSeed("SKU-LEN-T14G4", "Lenovo ThinkPad T14", "Computers", 1349, 1110, 7, 10),
        new ProductSeed("SKU-SAM-T7-2T", "Samsung T7 Shield 2TB", "Storage", 169, 118, 16, 15)
      );
      for (ProductSeed seed : catalog) {
        if (products.findBySku(seed.sku()).isEmpty()) {
          Product product = new Product(seed.sku(), seed.name(), seed.inventory(), seed.price());
          product.updateDetails(seed.name(), seed.sku(), seed.category() + " product", seed.price(), seed.cost(), seed.category(), seed.reorderLevel());
          products.save(product);
        }
      }
      if (suppliers.count() == 0) {
        suppliers.save(new Supplier("Northstar Distribution", "Maya Chen", "maya@northstar.example", "+1 415 555 0142", "San Francisco, CA"));
        suppliers.save(new Supplier("Apex Technology Supply", "Daniel Brooks", "daniel@apex.example", "+1 206 555 0188", "Seattle, WA"));
        suppliers.save(new Supplier("Lumen Audio Group", "Priya Nair", "priya@lumen.example", "+1 212 555 0194", "New York, NY"));
      }
      if (customers.count() == 0) {
        customers.save(new Customer("Olivia Bennett", "olivia.bennett@example.com", "+1 415 555 0101", "18 Market Street", "San Francisco", "CA", "94105"));
        customers.save(new Customer("Ethan Morgan", "ethan.morgan@example.com", "+1 206 555 0102", "204 Pine Street", "Seattle", "WA", "98101"));
        customers.save(new Customer("Ava Rodriguez", "ava.rodriguez@example.com", "+1 212 555 0103", "77 Hudson Avenue", "New York", "NY", "10013"));
        customers.save(new Customer("Noah Williams", "noah.williams@example.com", "+1 512 555 0104", "310 Congress Avenue", "Austin", "TX", "78701"));
      }
      if (movementsEmpty) {
        for (Product product : products.findAll()) movements.save(new InventoryMovement(product, MovementType.STOCK_IN, product.getTotalInventory(), 0, product.getAvailableInventory(), "Opening inventory history", "SEED-" + product.getSku(), "system"));
      }
    };
  }

  private record ProductSeed(String sku, String name, String category, double price, double cost, int inventory, int reorderLevel) {}
}

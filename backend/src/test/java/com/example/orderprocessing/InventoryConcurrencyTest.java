package com.example.orderprocessing;

import com.example.orderprocessing.dto.OrderRequest;
import com.example.orderprocessing.entity.OrderStatus;
import com.example.orderprocessing.entity.Product;
import com.example.orderprocessing.service.OrderService;
import com.example.orderprocessing.repository.OrderRepository;
import com.example.orderprocessing.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.concurrent.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class InventoryConcurrencyTest {
  @Autowired ProductRepository products;
  @Autowired OrderRepository orders;
  @Autowired OrderService orderService;

  @Test
  void inventoryNeverBecomesNegativeUnderConcurrentReservations() throws Exception {
    Product product = products.save(new Product("TEST-CONCURRENCY-" + System.nanoTime(), "Test product", 10, 1));
    for (int i = 0; i < 100; i++) orderService.create(new OrderRequest("Concurrency test", product.getId(), 1));
    Thread.sleep(3000);
    Product result = products.findById(product.getId()).orElseThrow();
    assertThat(orders.findTop100ByOrderByCreatedAtDesc().stream().filter(order -> order.getProduct().getId().equals(product.getId())).filter(order -> order.getStatus() == OrderStatus.COMPLETED).count()).isEqualTo(10);
    assertThat(result.getAvailableInventory()).isZero();
    assertThat(result.getAvailableInventory()).isGreaterThanOrEqualTo(0);
  }
}

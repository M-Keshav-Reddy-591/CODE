package com.example.orderprocessing.service;

import com.example.orderprocessing.entity.CustomerOrder;
import com.example.orderprocessing.repository.OrderRepository;
import com.example.orderprocessing.repository.ProductRepository;
import com.example.orderprocessing.repository.InventoryMovementRepository;
import com.example.orderprocessing.entity.MovementType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderProcessor {
  private final OrderRepository orders;
  private final ProductRepository products;
  private final EventHub events;
  private final InventoryMovementRepository movements;

  public OrderProcessor(OrderRepository orders, ProductRepository products, EventHub events, InventoryMovementRepository movements) {
    this.orders = orders;
    this.products = products;
    this.events = events;
    this.movements = movements;
  }

  @Async("orderExecutor")
  @Transactional
  public void process(Long orderId) {
    CustomerOrder order = orders.findById(orderId).orElseThrow();
    order.processing();
    events.publish(view(order));
    var product = products.findLocked(order.getProduct().getId()).orElseThrow();
    int before = product.getAvailableInventory();
    if (!product.reserve(order.getQuantity())) {
      order.outOfStock();
    } else {
      order.complete();
      movements.save(new com.example.orderprocessing.entity.InventoryMovement(product, MovementType.RESERVATION, -order.getQuantity(), before, product.getAvailableInventory(), "Order reservation", order.getOrderNumber(), "system"));
    }
    orders.save(order);
    events.publish(view(order));
  }

  private Object view(CustomerOrder order) {
    return java.util.Map.of("id", order.getId(), "orderNumber", order.getOrderNumber(), "customerName", order.getCustomerName(), "product", order.getProduct().getName(), "quantity", order.getQuantity(), "status", order.getStatus(), "retryCount", order.getRetryCount(), "failureReason", order.getFailureReason() == null ? "" : order.getFailureReason(), "updatedAt", order.getUpdatedAt());
  }
}

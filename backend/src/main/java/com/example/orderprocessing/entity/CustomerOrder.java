package com.example.orderprocessing.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name="orders")
public class CustomerOrder {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false,unique=true) private String orderNumber;
  @Column(nullable=false) private String customerName;
  @ManyToOne(fetch=FetchType.EAGER,optional=false) private Product product;
  @Column(nullable=false) private int quantity;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private OrderStatus status=OrderStatus.PENDING;
  private int retryCount; private String failureReason; private Instant createdAt=Instant.now(); private Instant processingStartedAt; private Instant completedAt; private Instant updatedAt=Instant.now();
  protected CustomerOrder() {}
  public CustomerOrder(String number,String customer,Product product,int quantity){this.orderNumber=number;this.customerName=customer;this.product=product;this.quantity=quantity;}
  public void processing(){status=OrderStatus.PROCESSING;processingStartedAt=Instant.now();updatedAt=Instant.now();}
  public void complete(){status=OrderStatus.COMPLETED;completedAt=Instant.now();updatedAt=Instant.now();}
  public void confirm(){if(status!=OrderStatus.PENDING)throw new IllegalStateException("Only pending orders can be confirmed");status=OrderStatus.CONFIRMED;updatedAt=Instant.now();}
  public void ship(){if(status!=OrderStatus.COMPLETED && status!=OrderStatus.PROCESSING)throw new IllegalStateException("Only processed orders can be shipped");status=OrderStatus.SHIPPED;updatedAt=Instant.now();}
  public void deliver(){if(status!=OrderStatus.SHIPPED)throw new IllegalStateException("Only shipped orders can be delivered");status=OrderStatus.DELIVERED;updatedAt=Instant.now();}
  public void cancel(){if(status==OrderStatus.DELIVERED||status==OrderStatus.CANCELLED)throw new IllegalStateException("Order cannot be cancelled");status=OrderStatus.CANCELLED;updatedAt=Instant.now();}
  public void outOfStock(){status=OrderStatus.OUT_OF_STOCK;failureReason="Insufficient inventory";updatedAt=Instant.now();}
  public void fail(String reason){retryCount++;failureReason=reason;status=retryCount>=3?OrderStatus.DEAD_LETTER:OrderStatus.RETRYING;updatedAt=Instant.now();}
  public void retry(){status=OrderStatus.PENDING;failureReason=null;updatedAt=Instant.now();}
  public Long getId(){return id;} public String getOrderNumber(){return orderNumber;} public String getCustomerName(){return customerName;} public Product getProduct(){return product;} public int getQuantity(){return quantity;} public OrderStatus getStatus(){return status;} public int getRetryCount(){return retryCount;} public String getFailureReason(){return failureReason;} public Instant getCreatedAt(){return createdAt;} public Instant getProcessingStartedAt(){return processingStartedAt;} public Instant getCompletedAt(){return completedAt;} public Instant getUpdatedAt(){return updatedAt;}
}

package com.example.orderprocessing.repository;
import com.example.orderprocessing.entity.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface OrderRepository extends JpaRepository<CustomerOrder,Long> { List<CustomerOrder> findTop100ByOrderByCreatedAtDesc(); List<CustomerOrder> findByStatusInOrderByUpdatedAtDesc(Collection<OrderStatus> statuses); long countByStatus(OrderStatus status); }

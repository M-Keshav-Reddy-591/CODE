package com.example.orderprocessing.repository;

import com.example.orderprocessing.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
  List<Customer> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrderByNameAsc(String name, String email);
}

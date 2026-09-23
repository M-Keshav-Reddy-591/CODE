package com.example.orderprocessing.repository;

import com.example.orderprocessing.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
  List<Supplier> findByCompanyNameContainingIgnoreCaseOrContactPersonContainingIgnoreCaseOrderByCompanyNameAsc(String companyName, String contactPerson);
}

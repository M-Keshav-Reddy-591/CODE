package com.example.orderprocessing.service;

import com.example.orderprocessing.dto.SupplierRequest;
import com.example.orderprocessing.entity.Supplier;
import com.example.orderprocessing.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SupplierService {
  private final SupplierRepository repo;
  public SupplierService(SupplierRepository repo){this.repo=repo;}
  public List<Supplier> all(String query){return query==null||query.isBlank()?repo.findAll():repo.findByCompanyNameContainingIgnoreCaseOrContactPersonContainingIgnoreCaseOrderByCompanyNameAsc(query,query);}
  public Supplier get(Long id){return repo.findById(id).orElseThrow();}
  public Supplier create(SupplierRequest r){return repo.save(new Supplier(r.companyName(),r.contactPerson(),r.email(),r.phone(),r.address()));}
  public Supplier update(Long id,SupplierRequest r){Supplier s=get(id);s.update(r.companyName(),r.contactPerson(),r.email(),r.phone(),r.address());return repo.save(s);}
  public Supplier deactivate(Long id){Supplier s=get(id);s.deactivate();return repo.save(s);}
}

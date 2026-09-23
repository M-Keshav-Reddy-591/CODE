package com.example.orderprocessing.service;

import com.example.orderprocessing.dto.CustomerRequest;
import com.example.orderprocessing.entity.Customer;
import com.example.orderprocessing.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerService {
  private final CustomerRepository repo;
  public CustomerService(CustomerRepository repo){this.repo=repo;}
  public List<Customer> all(String query){return query==null||query.isBlank()?repo.findAll():repo.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrderByNameAsc(query,query);}
  public Customer get(Long id){return repo.findById(id).orElseThrow();}
  public Customer create(CustomerRequest r){return repo.save(new Customer(r.name(),r.email(),r.phone(),r.address(),r.city(),r.state(),r.postalCode()));}
  public Customer update(Long id,CustomerRequest r){Customer c=get(id);c.update(r.name(),r.email(),r.phone(),r.address(),r.city(),r.state(),r.postalCode());return repo.save(c);}
  public Customer deactivate(Long id){Customer c=get(id);c.deactivate();return repo.save(c);}
}

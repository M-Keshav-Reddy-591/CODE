package com.example.orderprocessing.controller;

import com.example.orderprocessing.dto.CustomerRequest;
import com.example.orderprocessing.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
  private final CustomerService service;
  public CustomerController(CustomerService service){this.service=service;}
  @GetMapping public Object all(@RequestParam(required=false) String search){return service.all(search);}
  @GetMapping("/{id}") public Object get(@PathVariable Long id){return service.get(id);}
  @PostMapping public Object create(@Valid @RequestBody CustomerRequest request){return service.create(request);}
  @PutMapping("/{id}") public Object update(@PathVariable Long id,@Valid @RequestBody CustomerRequest request){return service.update(id,request);}
  @DeleteMapping("/{id}") public Object deactivate(@PathVariable Long id){return service.deactivate(id);}
}

package com.example.orderprocessing.controller;

import com.example.orderprocessing.dto.SupplierRequest;
import com.example.orderprocessing.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {
  private final SupplierService service;
  public SupplierController(SupplierService service){this.service=service;}
  @GetMapping public Object all(@RequestParam(required=false) String search){return service.all(search);}
  @GetMapping("/{id}") public Object get(@PathVariable Long id){return service.get(id);}
  @PostMapping public Object create(@Valid @RequestBody SupplierRequest request){return service.create(request);}
  @PutMapping("/{id}") public Object update(@PathVariable Long id,@Valid @RequestBody SupplierRequest request){return service.update(id,request);}
  @DeleteMapping("/{id}") public Object deactivate(@PathVariable Long id){return service.deactivate(id);}
}

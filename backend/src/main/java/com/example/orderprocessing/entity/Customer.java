package com.example.orderprocessing.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "customers", indexes = @Index(name = "idx_customer_email", columnList = "email"))
public class Customer {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
  @Column(nullable = false) private String name;
  @Column(unique = true) private String email;
  private String phone;
  private String address;
  private String city;
  private String state;
  private String postalCode;
  @Enumerated(EnumType.STRING) @Column(nullable = false) private RecordStatus status = RecordStatus.ACTIVE;
  private Instant createdAt = Instant.now();
  private Instant updatedAt = Instant.now();
  protected Customer() {}
  public Customer(String name, String email, String phone, String address, String city, String state, String postalCode) { this.name=name; this.email=email; this.phone=phone; this.address=address; this.city=city; this.state=state; this.postalCode=postalCode; }
  public void update(String name, String email, String phone, String address, String city, String state, String postalCode) { this.name=name; this.email=email; this.phone=phone; this.address=address; this.city=city; this.state=state; this.postalCode=postalCode; this.updatedAt=Instant.now(); }
  public void deactivate() { status=RecordStatus.INACTIVE; updatedAt=Instant.now(); }
  public Long getId(){return id;} public String getName(){return name;} public String getEmail(){return email;} public String getPhone(){return phone;} public String getAddress(){return address;} public String getCity(){return city;} public String getState(){return state;} public String getPostalCode(){return postalCode;} public RecordStatus getStatus(){return status;} public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;}
}

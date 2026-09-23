package com.example.orderprocessing.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "suppliers")
public class Supplier {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
  @Column(nullable = false) private String companyName;
  private String contactPerson;
  private String email;
  private String phone;
  private String address;
  @Enumerated(EnumType.STRING) @Column(nullable = false) private RecordStatus status = RecordStatus.ACTIVE;
  private Instant createdAt = Instant.now();
  protected Supplier() {}
  public Supplier(String companyName, String contactPerson, String email, String phone, String address) { this.companyName=companyName; this.contactPerson=contactPerson; this.email=email; this.phone=phone; this.address=address; }
  public void update(String companyName, String contactPerson, String email, String phone, String address) { this.companyName=companyName; this.contactPerson=contactPerson; this.email=email; this.phone=phone; this.address=address; }
  public void deactivate() { status=RecordStatus.INACTIVE; }
  public Long getId(){return id;} public String getCompanyName(){return companyName;} public String getContactPerson(){return contactPerson;} public String getEmail(){return email;} public String getPhone(){return phone;} public String getAddress(){return address;} public RecordStatus getStatus(){return status;} public Instant getCreatedAt(){return createdAt;}
}

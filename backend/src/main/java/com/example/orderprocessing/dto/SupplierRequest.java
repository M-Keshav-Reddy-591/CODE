package com.example.orderprocessing.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SupplierRequest(@NotBlank String companyName, String contactPerson, @Email String email, String phone, String address) {}

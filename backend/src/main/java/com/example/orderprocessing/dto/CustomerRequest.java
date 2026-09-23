package com.example.orderprocessing.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerRequest(@NotBlank String name, @Email String email, String phone, String address, String city, String state, String postalCode) {}

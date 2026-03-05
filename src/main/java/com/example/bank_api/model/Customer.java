package com.example.bank_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer")
public class Customer {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @Size(max = 255)
    private String name;
    @Column(unique = true, nullable = false)
    @Size(max = 20)
    private String documentNumber;
    @Column(unique = true, nullable = false)
    private String email;
    @NotNull
    @Size(max = 255, min = 6)
    private String password;

    @Column(name = "is_business")
    private boolean isBusiness;
}
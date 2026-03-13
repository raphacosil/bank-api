package com.example.bank_api.model;

import jakarta.persistence.*;
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
@Entity
@Table(name = "customer")
public class Customer {
    @Id
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
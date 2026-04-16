package com.example.bank_api.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transference")
public class Transference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "payer")
    private Long payer;

    @NotNull
    @Column(name = "payee")
    private Long payee;

    @NotNull
    @Column(name = "value")
    private Double value;

    @Column(name = "created_at")
    private Date createdAt;
}

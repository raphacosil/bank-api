package com.example.bank_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transference")
public class Transference {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @JoinColumn
    @OneToOne(mappedBy = "payer_id")
    private Long payer;
    @NotNull
    @JoinColumn
    @OneToOne(mappedBy = "receiver_id")
    private Long payee;
    @NotNull
    private Double value;
    @Column(name = "created_at")
    private Date createdAt;
}

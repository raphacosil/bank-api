package com.example.bank_api.model;

import jakarta.persistence.*;
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
    @JoinColumn
    @OneToOne(mappedBy = "payer_id")
    private Long payer;
    @JoinColumn
    @OneToOne(mappedBy = "receiver_id")
    private Long payee;
    private Double value;
    @Column(name = "created_at")
    private Date createdAt;
}

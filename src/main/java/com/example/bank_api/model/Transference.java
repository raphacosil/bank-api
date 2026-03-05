package com.example.bank_api.model;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transference")
public class Transference {
    private Long id;
    @JoinColumn
    @OneToOne(mappedBy = "payer_id")
    private Long payer_id;
    @JoinColumn
    @OneToOne(mappedBy = "receiver_id")
    private Long receiver_id;
    private Double amount;
}

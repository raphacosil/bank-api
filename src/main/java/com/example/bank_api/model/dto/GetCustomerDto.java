package com.example.bank_api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class GetCustomerDto {
    private Long id;
    private String name;
    private String documentNumber;
    private String email;
    private boolean isBusiness;
}

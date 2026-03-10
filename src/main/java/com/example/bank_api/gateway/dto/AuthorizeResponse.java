package com.example.bank_api.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizeResponse {
    String status;
    AuthorizeResponseData data;
}

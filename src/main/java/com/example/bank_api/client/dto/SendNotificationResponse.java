package com.example.bank_api.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SendNotificationResponse {
    String status;
    SendNotificationResponseData notificationData;
}

package com.TicketXChange.TicketXChange.auth.dtos;


import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordRequest {
    @NonNull
    private  String email;
}

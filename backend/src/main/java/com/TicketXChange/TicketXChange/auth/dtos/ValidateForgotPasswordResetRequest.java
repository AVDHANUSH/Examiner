package com.TicketXChange.TicketXChange.auth.dtos;


import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidateForgotPasswordResetRequest {
    @NonNull
    private String email;
    @NonNull
    private String newPassword;
    @NonNull
    private String newPasswordConfirm;
}

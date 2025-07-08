package com.TicketXChange.TicketXChange.auth.dtos;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class  PasswordResetRequest {
    @NonNull
    private String existingPassword;
    @NonNull
    private String newPassword;
    @NonNull
    private String newPasswordConfirm;

}




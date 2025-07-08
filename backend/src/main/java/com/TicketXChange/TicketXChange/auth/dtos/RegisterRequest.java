package com.TicketXChange.TicketXChange.auth.dtos;


import com.TicketXChange.TicketXChange.auth.model.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @Getter
    @Setter
    @NonNull
    private String fullName;
    @NonNull
    private  String email;
    @NonNull
    private String  password;
    @NonNull
    private String confirmpassword;
    private Role role;
}

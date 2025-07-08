package com.TicketXChange.TicketXChange.auth.dtos;
import com.TicketXChange.TicketXChange.auth.enums.ReasonEnum;
import com.TicketXChange.TicketXChange.auth.model.Reason;
import lombok.*;
import org.jetbrains.annotations.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeactivateOrDeleteRequest {

    @NotNull
    private ReasonEnum reasonEnum;
    private String description;
    @NotNull
    private String Password;

}

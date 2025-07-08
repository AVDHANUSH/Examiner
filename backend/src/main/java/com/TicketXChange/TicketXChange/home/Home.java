package com.TicketXChange.TicketXChange.home;

import com.TicketXChange.TicketXChange.auth.dtos.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/v1/home")
@RequiredArgsConstructor
public class Home {

    @GetMapping("")
    public ResponseEntity<ApiResponse> Home(){
        return ResponseEntity.ok().body(ApiResponse
                .builder()
                .message("successfully loggged in")
                .build());
    }
}

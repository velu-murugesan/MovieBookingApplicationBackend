package com.velu.MovieBookingApplication.dtoresponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponse {

    private String jwtToken;
    private String username;
    private Set<String> roles;

}

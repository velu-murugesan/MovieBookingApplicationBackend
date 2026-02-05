package com.velu.MovieBookingApplication.dto;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class LoginRequestDto {
    @NotBlank(message = "username should not be blank")
    private String username;
    @NotBlank(message = "password should not be blank")
    @Min(value = 6,message = "Password length is must be 6 or greater")
    private String password;
}

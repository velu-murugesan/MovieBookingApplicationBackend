package com.velu.MovieBookingApplication.dto;
import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class RegisterRequestDTO {
    @NotBlank(message = "username should not be blank")
    private String username;
    @NotNull(message = "email is required")
    @Email(message = "your email is not valid")
    private String email;
    @NotBlank(message = "password should not be blank")
    @Min(value = 6,message = "Password length is must be 6 or greater")
    private String password;
}

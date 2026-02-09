package com.velu.MovieBookingApplication.dtoresponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterResponseDto {
    private Long id;
    private String username;
    private Set<String> roles;
    private String email;
}

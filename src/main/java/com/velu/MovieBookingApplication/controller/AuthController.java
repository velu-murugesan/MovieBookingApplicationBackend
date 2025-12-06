package com.velu.MovieBookingApplication.controller;


import com.velu.MovieBookingApplication.dto.LoginRequestDto;
import com.velu.MovieBookingApplication.dto.LoginResponseDTO;
import com.velu.MovieBookingApplication.dto.RegisterRequestDTO;
import com.velu.MovieBookingApplication.entity.User;
import com.velu.MovieBookingApplication.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/registernormaluser")
    public ResponseEntity<User> registerNormalUser(@RequestBody RegisterRequestDTO registerRequestDTO){
          return ResponseEntity.ok(authenticationService.registerNormalUser(registerRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO>  login(@RequestBody LoginRequestDto loginRequestDto){
        return ResponseEntity.ok(authenticationService.login(loginRequestDto));
    }

}

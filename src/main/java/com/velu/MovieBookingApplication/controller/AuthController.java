package com.velu.MovieBookingApplication.controller;
import com.velu.MovieBookingApplication.dto.LoginRequestDto;
import com.velu.MovieBookingApplication.dtoresponse.LoginResponse;
import com.velu.MovieBookingApplication.dto.RegisterRequestDTO;
import com.velu.MovieBookingApplication.dtoresponse.UserRegisterResponseDto;
import com.velu.MovieBookingApplication.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDto> registerNormalUser(@Valid @RequestBody RegisterRequestDTO registerRequestDTO){
          return new ResponseEntity<UserRegisterResponseDto>(authenticationService.registerNormalUser(registerRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse>  login(@Valid @RequestBody LoginRequestDto loginRequestDto){
        return ResponseEntity.ok(authenticationService.login(loginRequestDto));
    }

}

package com.velu.MovieBookingApplication.controller;
import com.velu.MovieBookingApplication.dto.RegisterRequestDTO;
import com.velu.MovieBookingApplication.dtoresponse.UserRegisterResponseDto;
import com.velu.MovieBookingApplication.entity.User;
import com.velu.MovieBookingApplication.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping()
    public ResponseEntity<UserRegisterResponseDto> registerAdminUser(@Valid @RequestBody RegisterRequestDTO registerRequestDTO){
        return new ResponseEntity<UserRegisterResponseDto>(authenticationService.registerAdminUser(registerRequestDTO), HttpStatus.CREATED);
    }
}

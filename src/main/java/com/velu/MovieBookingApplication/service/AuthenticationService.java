package com.velu.MovieBookingApplication.service;
import com.velu.MovieBookingApplication.Repository.UserRepository;
import com.velu.MovieBookingApplication.dto.LoginRequestDto;
import com.velu.MovieBookingApplication.dto.RegisterRequestDTO;
import com.velu.MovieBookingApplication.dtoresponse.LoginResponse;
import com.velu.MovieBookingApplication.dtoresponse.UserRegisterResponseDto;
import com.velu.MovieBookingApplication.entity.User;
import com.velu.MovieBookingApplication.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthenticationService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    public UserRegisterResponseDto registerNormalUser(RegisterRequestDTO registerRequestDTO) {

        if(userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()){
             throw new ResponseStatusException(HttpStatus.CONFLICT,"User already registered");
        }

        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");

        User user = new User();
        user.setEmail(registerRequestDTO.getEmail());
        user.setUsername(registerRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setRoles(roles);

        return Utils.convertUserToUserResponse(userRepository.save(user));
    }

    public UserRegisterResponseDto registerAdminUser(RegisterRequestDTO registerRequestDTO) {

        if(userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"User already registered");
        }

        Set<String> roles = new HashSet<>();
        roles.add("ROLE_ADMIN");
        roles.add("ROLE_USER");
        User user = new User();
        user.setEmail(registerRequestDTO.getEmail());
        user.setUsername(registerRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setRoles(roles);

        return Utils.convertUserToUserResponse(userRepository.save(user));
    }

    public LoginResponse login(LoginRequestDto loginRequestDto) {
       User user =  userRepository.findByUsername(loginRequestDto.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not Exist"));



       authenticationManager.authenticate(

               new UsernamePasswordAuthenticationToken(
                       loginRequestDto.getUsername(),
                       loginRequestDto.getPassword()
               )
       );

       String token = jwtService.generateToken(user);

       return LoginResponse.builder()
               .jwtToken(token)
               .username(user.getUsername())
               .roles(user.getRoles())
               .build();
    }
}

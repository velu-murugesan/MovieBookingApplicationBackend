package com.velu.MovieBookingApplication.service;
import com.velu.MovieBookingApplication.Repository.UserRepository;
import com.velu.MovieBookingApplication.dto.LoginRequestDto;
import com.velu.MovieBookingApplication.dto.LoginResponseDTO;
import com.velu.MovieBookingApplication.dto.RegisterRequestDTO;
import com.velu.MovieBookingApplication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

    public User registerNormalUser(RegisterRequestDTO registerRequestDTO) {

        if(userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()){
             throw new RuntimeException("User already registered");
        }

        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");

        User user = new User();
        user.setEmail(registerRequestDTO.getEmail());
        user.setUsername(registerRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setRoles(roles);

        return userRepository.save(user);
    }

    public User registerAdminUser(RegisterRequestDTO registerRequestDTO) {

        if(userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()){
            throw new RuntimeException("User already registered");
        }

        Set<String> roles = new HashSet<>();
        roles.add("ROLE_ADMIN");
        roles.add("ROLE_USER");
        User user = new User();
        user.setEmail(registerRequestDTO.getEmail());
        user.setUsername(registerRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setRoles(roles);

        return userRepository.save(user);
    }

    public LoginResponseDTO login(LoginRequestDto loginRequestDto) {
       User user =  userRepository.findByUsername(loginRequestDto.getUsername()).orElseThrow(() -> new RuntimeException("User not present"));



       authenticationManager.authenticate(

               new UsernamePasswordAuthenticationToken(
                       loginRequestDto.getUsername(),
                       loginRequestDto.getPassword()
               )
       );

       String token = jwtService.generateToken(user);

       return LoginResponseDTO.builder()
               .jwtToken(token)
               .username(user.getUsername())
               .roles(user.getRoles())
               .build();
    }
}

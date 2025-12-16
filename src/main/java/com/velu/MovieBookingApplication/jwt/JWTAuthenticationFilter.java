package com.velu.MovieBookingApplication.jwt;


import com.velu.MovieBookingApplication.Repository.UserRepository;
import com.velu.MovieBookingApplication.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String username;

        System.out.println("PATH = " + request.getRequestURI());
        System.out.println("AUTH = " + request.getHeader("Authorization"));


        if(authHeader == null || !authHeader.startsWith("Bearer")){
            filterChain.doFilter(request,response);
            return ;
        }

       jwtToken =  authHeader.substring(7);
       username =  jwtService.extractUsername(jwtToken);

       if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){

           var userdetails = userRepository.findByUsername(username)
                   .orElseThrow(() -> new RuntimeException("user not found"));

           if(jwtService.isTokenValid(jwtToken,userdetails)){
               List<SimpleGrantedAuthority> authorities = userdetails.getRoles().stream()
                       .map(SimpleGrantedAuthority::new)
                       .collect(Collectors.toList());

               UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userdetails,null,authorities);

               authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

               SecurityContextHolder.getContext().setAuthentication(authToken);

           }



       }

       filterChain.doFilter(request,response);

    }

}

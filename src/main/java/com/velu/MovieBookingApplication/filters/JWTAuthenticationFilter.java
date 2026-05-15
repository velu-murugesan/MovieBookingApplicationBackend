package com.velu.MovieBookingApplication.filters;
import com.velu.MovieBookingApplication.Repository.UserRepository;
import com.velu.MovieBookingApplication.service.CustomUserDetailsService;
import com.velu.MovieBookingApplication.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collection;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

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

           var userdetails = customUserDetailsService.loadUserByUsername(username);

           if(jwtService.isTokenValid(jwtToken,userdetails)){
               Collection<? extends GrantedAuthority> authorities =  userdetails.getAuthorities();

               UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userdetails,null,authorities);

               authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

               SecurityContextHolder.getContext().setAuthentication(authToken);

           }

       }

       filterChain.doFilter(request,response);

    }

}

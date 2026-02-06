package com.velu.MovieBookingApplication.filters;
import com.velu.MovieBookingApplication.bucket4j.BucketStore;
import com.velu.MovieBookingApplication.bucket4j.RateLimitRules;
import com.velu.MovieBookingApplication.exception.RateLimitExceededException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String path = request.getRequestURI();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = auth.getName();

        String key = user + ":" + path;

        Bandwidth limit;

        if(path.startsWith("/api/auth")){
            limit = RateLimitRules.LOGIN;
        }else if(path.startsWith("/api/bookings")){
            limit = RateLimitRules.BOOKING;
        }else{
            limit = RateLimitRules.GLOBAL;
        }

      Bucket bucket = BucketStore.getBucket(key,limit);

        if(bucket.tryConsume(1)){
            filterChain.doFilter(request,response);
        }else{
            throw new RateLimitExceededException("you have request too many time within certain time period");
        }

    }
}

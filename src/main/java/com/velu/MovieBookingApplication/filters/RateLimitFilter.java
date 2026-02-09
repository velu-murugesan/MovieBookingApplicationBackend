package com.velu.MovieBookingApplication.filters;
import com.velu.MovieBookingApplication.bucket4j.BucketStore;
import com.velu.MovieBookingApplication.bucket4j.RateLimitRules;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
@Order(1)
public class RateLimitFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String key = request.getRemoteAddr();
        String path = request.getRequestURI();
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
            return;
        }

        response.reset();
        response.setContentType("application/json");
        response.setStatus(429);

        response.getWriter().write("""
        {
          "timestamp": "%s",
          "status": 429,
          "error": "Too Many Requests",
          "message": "Rate limit exceeded. Try again later.",
          "path": "%s"
        }
        """.formatted(
                LocalDateTime.now(),
                request.getRequestURI()
        ));


  return;
    }


}

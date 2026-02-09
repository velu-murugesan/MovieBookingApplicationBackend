package com.velu.MovieBookingApplication.util;
import com.velu.MovieBookingApplication.dtoresponse.LoginResponse;
import com.velu.MovieBookingApplication.dtoresponse.PaginationResponse;
import com.velu.MovieBookingApplication.dtoresponse.UserRegisterResponseDto;
import com.velu.MovieBookingApplication.entity.User;
import org.springframework.data.domain.Page;
import java.util.function.Function;

import static io.jsonwebtoken.Jwts.builder;


public class Utils {

       public static <T> PaginationResponse<T> convertPageToPaginationResponse(
               Page<T> page
       ){

           return PaginationResponse.<T>builder()
                   .content(page.getContent())
                   .last(page.isLast())
                   .pageSize(page.getSize())
                   .totalElements(page.getNumberOfElements())
                   .totalPages(page.getTotalPages())
                   .totalSize(page.getSize())
                   .build();

       }


       public static UserRegisterResponseDto convertUserToUserResponse(User user){

           return UserRegisterResponseDto.builder()
                   .id(user.getId())
                   .roles(user.getRoles())
                   .email(user.getEmail())
                   .username(user.getUsername())
                   .build();


       }



}

package com.velu.MovieBookingApplication.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class RootController {

    @GetMapping
    public String getDate(){
        return "Welcome to the moviesDA and choose your favourite movie";
    }

}

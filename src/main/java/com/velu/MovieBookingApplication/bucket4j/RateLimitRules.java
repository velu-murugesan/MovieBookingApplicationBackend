package com.velu.MovieBookingApplication.bucket4j;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;

import java.time.Duration;

public class RateLimitRules {

    public static final Bandwidth GLOBAL = Bandwidth.classic(50, Refill.intervally(50, Duration.ofMinutes(1)));
    public static final Bandwidth BOOKING = Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(1)));
    public static final Bandwidth LOGIN = Bandwidth.classic(5, Refill.intervally(2, Duration.ofMinutes(1)));
}

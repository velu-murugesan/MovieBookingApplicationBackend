package com.velu.MovieBookingApplication.bucket4j;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class BucketStore {

     private static final Map<String, Bucket> BUCKETS = new ConcurrentHashMap<>();

     public static Bucket getBucket(String key, Bandwidth limit){

         return  BUCKETS.computeIfAbsent(
                 key, k -> Bucket.builder().addLimit(limit).build()
         );

     }

}

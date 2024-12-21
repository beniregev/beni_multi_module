package com.beniregev.serviceshuffle.controller;

import com.beniregev.serviceshuffle.service.ShuffleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/api/shuffle")
@RestController
public class ShuffleController {
   private static int MAX_SHUFFLED_ARRAY_SIZE = 1000;

   private final ShuffleService shuffleService;

   public ShuffleController(ShuffleService shuffleService) {
      this.shuffleService = shuffleService;
   }

   @Operation(summary = "Create a shuffled array of integers between 1 an value of size, without duplicates")
   @ApiResponses(value = {
           @ApiResponse(responseCode = "201", description = "Shuffled array of integers created successfully"),
           @ApiResponse(responseCode = "422", description = "either the value of size is not between 1 and 1000, or the shuffled array size is zero"),
           @ApiResponse(responseCode = "500", description = "Some other exception occurred while logging the request body")
   })
   @PostMapping(path="/v1/{size}")
   public ResponseEntity<Integer[]> createShuffledArray(@PathVariable final int size) {
      if ((size < 1) || (size > MAX_SHUFFLED_ARRAY_SIZE)) {
         throw new IllegalArgumentException("Size of the array must be between 1 and " + MAX_SHUFFLED_ARRAY_SIZE);
      }
      return new ResponseEntity(shuffleService.generateAndLogShuffledArray(size), HttpStatus.CREATED);
   }

   @Operation(summary = "Ping the service-shuffle server")
   @ApiResponses(value = {
           @ApiResponse(responseCode = "200", description = "service-shuffle server pinged successfully - it's up and running")
   })
   @GetMapping(path = "/v1/ping")
   public ResponseEntity<String> ping() {
      return ResponseEntity.ok("Shuffle-Service server is running, ready to receive and process requests.");
   }

   @Operation(summary = "Ping the service-shuffle service layer")
   @ApiResponses(value = {
           @ApiResponse(responseCode = "200", description = "service-shuffle service layer pinged successfully - it's up and running")
   })
   @GetMapping(path = "/v1/ping-service")
   public ResponseEntity<String> pingService() {
      return ResponseEntity.ok(shuffleService.pingService());
   }

}

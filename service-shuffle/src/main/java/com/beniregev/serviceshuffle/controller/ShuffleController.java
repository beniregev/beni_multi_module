package com.beniregev.serviceshuffle.controller;

import com.beniregev.serviceshuffle.service.ShuffleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/api/shuffle")
@RestController
public class ShuffleController {
   @Value("${shuffled.controller.array.size.max")
   private static int MAX_SHUFFLED_ARRAY_SIZE = 1000;

   private final ShuffleService shuffleService;

   public ShuffleController(ShuffleService shuffleService) {
      this.shuffleService = shuffleService;
   }

   @PostMapping(path="/v1/{size}")
   public ResponseEntity<Integer[]> createShuffledArray(@PathVariable final int size) {
      if ((size < 1) || (size > MAX_SHUFFLED_ARRAY_SIZE)) {
         throw new IllegalArgumentException("Size of the array must be between 1 and " + MAX_SHUFFLED_ARRAY_SIZE);
      }
      return new ResponseEntity(shuffleService.generateShuffledArray(size), HttpStatus.CREATED);
   }

   @GetMapping(path = "/v1/ping")
   public ResponseEntity<String> ping() {
      return ResponseEntity.ok("Shuffle-Service server is running, ready to receive and process requests.");
   }

   @GetMapping(path = "/v1/ping-service")
   public ResponseEntity<String> pingService() {
      return ResponseEntity.ok(shuffleService.pingService());
   }

}

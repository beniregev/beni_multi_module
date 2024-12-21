package com.beniregev.servicelog.controller;

import com.beniregev.servicelog.service.LogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/api/log")
@RestController
public class LogController {
   private final LogService logService;

   public LogController(LogService logService) {
      this.logService = logService;
   }

   @PostMapping("/v1/shuffledArray")
   public ResponseEntity<String> createLogMessage(@RequestBody String body) {
      String result = logService.createLogMessageShuffledArray(body);
      return ResponseEntity.ok(result);
   }

   @GetMapping(path = "/v1/ping")
   public ResponseEntity<String> ping() {
      return ResponseEntity.ok("Log-Service server is running, ready to receive and process requests.");
   }

   @GetMapping(path = "/v1/ping-service")
   public ResponseEntity<String> pingService() {
      return ResponseEntity.ok(logService.pingService());
   }
}

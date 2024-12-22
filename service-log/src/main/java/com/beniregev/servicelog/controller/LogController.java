package com.beniregev.servicelog.controller;

import com.beniregev.servicelog.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping(value = "/api/log")
@RestController
public class LogController {
   private final LogService logService;

   public LogController(LogService logService) {
      this.logService = logService;
   }

   @Operation(summary = "Log the request body (on the console")
   @ApiResponses(value = {
           @ApiResponse(responseCode = "200", description = "The request body was logged successfully to the console (for the demo)"),
           @ApiResponse(responseCode = "422", description = "The request body is empty and an IllegalArgumentException is thrown or the request body is missing either 'logLevel' or 'message' properties, or both, and a NoSuchFieldException was thrown"),
           @ApiResponse(responseCode = "500", description = "Some other exception occurred while logging the request body")
   })
   @PostMapping("/v1/shuffledArray")
   public ResponseEntity<Map<String, Object>> createLogMessage(@RequestBody String body) throws NoSuchFieldException {
      if (body.isEmpty())
         throw new IllegalArgumentException("Request body is empty");
      if (!body.contains("\"sending\":") || !body.contains("\"logLevel\":") || !body.contains("\"message\":"))
         throw new NoSuchFieldException("Request body does not contain one or more required properties 'sending' or 'logLevel' or 'message'");
      Map<String, Object> result = logService.createLogMessageShuffledArray(body);
      return ResponseEntity.ok(result);
   }

   @Operation(summary = "Log the request body (on the console")
   @ApiResponses(value = {
           @ApiResponse(responseCode = "200", description = "The request body was logged successfully to the console (for the demo)")
   })
   @GetMapping(path = "/v1/ping")
   public ResponseEntity<String> ping() {
      return ResponseEntity.ok("Log-Service server is running, ready to receive and process requests.");
   }

   @Operation(summary = "Ping the service-log service layer")
   @ApiResponses(value = {
           @ApiResponse(responseCode = "200", description = "service-log service layer pinged successfully - it's up and running")
   })
   @GetMapping(path = "/v1/ping-service")
   public ResponseEntity<String> pingService() {
      return ResponseEntity.ok(logService.pingService());
   }
}

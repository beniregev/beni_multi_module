package com.beniregev.servicelog.service.impl;

import com.beniregev.servicelog.service.LogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Log4j
@Service
public class LogServiceImpl implements LogService {
   @Override
   public Map<String, Object> createLogMessageShuffledArray(String stringJSON) {
      ObjectMapper mapper = new ObjectMapper();
      // Convert JSON string to Map
      Map<String, Object> map;
      try {
         map = mapper.readValue(stringJSON, new TypeReference<>() {});
      } catch (JsonProcessingException e) {
         throw new RuntimeException(e);
      }
      map.put("timestamp", LocalDateTime.now());
      StringBuffer sbMessage = new StringBuffer(map.get("timestamp").toString())
              .append(" :: ")
              .append(map.get("sending").toString())
              .append(" Call -- ")
              .append(map.get("logLevel").toString())
              .append(": ")
              .append(map.get("message").toString());

      // The following code should be replaced with code to LOG the message properly
      System.out.println("message content: " + map);
      System.out.println("stringJSON: " + sbMessage.toString());
      return map;
   }

   /**
    * Return a String to acknowledge the code reached and received by the service.
    * @return a String to acknowledge the code reached and received by the service
    */
   @Override
   public String pingService() {
      return "Log service is running and ready to process requests.";
   }
}

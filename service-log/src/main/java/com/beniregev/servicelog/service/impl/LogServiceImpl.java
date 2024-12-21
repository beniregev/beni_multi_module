package com.beniregev.servicelog.service.impl;

import com.beniregev.servicelog.service.LogService;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

@Log4j
@Service
public class LogServiceImpl implements LogService {
   @Override
   public String createLogMessageShuffledArray(String message) {
      System.out.println(message);
      return message;
   }

   @Override
   public String pingService() {
      return "Log service is running and ready to process requests.";
   }
}

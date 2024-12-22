package com.beniregev.servicelog.service;

import java.util.Map;

public interface LogService {
   Map<String, Object> createLogMessageShuffledArray(String message);
   String pingService();
}

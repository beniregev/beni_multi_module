package com.beniregev.serviceshuffle.service;

import java.util.Map;

public interface ShuffleService {
   Map<String, Object> generateAndLogShuffledArray(int size);
   Integer[] generateShuffledArray(int size);
   String pingService();
}

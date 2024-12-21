package com.beniregev.serviceshuffle.service;

public interface ShuffleService {
   String generateAndLogShuffledArray(int size);
   Integer[] generateShuffledArray(int size);
   String pingService();
}

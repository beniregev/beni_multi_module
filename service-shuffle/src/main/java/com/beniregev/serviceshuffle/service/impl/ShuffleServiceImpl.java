package com.beniregev.serviceshuffle.service.impl;

import com.beniregev.serviceshuffle.service.ShuffleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


@Service
public class ShuffleServiceImpl implements ShuffleService {

   @Value("${log.service.protocol}")
   private String logServiceProtocol;
   @Value("${log.service.host}")
   private String logServiceHost;
   @Value("${log.service.port}")
   private String logServicePort;
   @Value("${log.service.path}")
   private String logServicePath;

   private static final Random RND = new Random();
   private final HttpClient client = HttpClient.newHttpClient();

   /**
    * <div>
    *    <p>
    *       This method does the flow of the exercise End-to-End (E2E):
    *       <ul>
    *          <li>Calling method {@link #generateShuffledArray(int)} to create the shuffled array <b>without duplicates</b>.</li>
    *          <li>Calling method {@link #generateLogMessagePostRequest(String)} to generate the {@link HttpRequest} to log the generated shuffled array.</li>
    *          <li>Sending the REST API POST request to the {@code service-log}, handling possible exception(s).</li>
    *          <li>Handling the returned {@link HttpResponse} checking HttpStatus code and response body.</li>
    *          <li>Returning the response body to the {@link com.beniregev.serviceshuffle.controller.ShuffleController} as a {@link String}.</li>
    *       </ul>
    *    </p>
    * </div>
    * @param size The size of the shuffled array, and the high-limit of the range of integers populating the array.
    * @return String. The response result (HTTP Status code and response body) of the REST API POST request call.
    */
   @Override
   public String generateAndLogShuffledArray(int size) {
      Integer[] arrIntegers = generateShuffledArray(size);
      if (arrIntegers.length < 1) {
         throw new ArrayStoreException("Shuffled integers array length is " + arrIntegers.length);
      }
      HttpRequest request = generateLogMessagePostRequest("Shuffled array with " + size + "elements: " + Arrays.toString(arrIntegers));
      StringBuffer sbResponseBody;
      try {
         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         if (response.statusCode()/100 != 2) {
            sbResponseBody = new StringBuffer("Calling POST requests FAILED.")
                    .append("\nHTTP Status code: ")
                    .append(response.statusCode())
                    .append("\nResponse Body: \n")
                    .append(response.body());
         } else {
            sbResponseBody = new StringBuffer("Calling POST requests SUCCESSFUL.")
                    .append("\nHTTP Status code: ")
                    .append(response.statusCode())
                    .append("\nResponse Body: \n")
                    .append(response.body());
         }
      } catch (IOException | InterruptedException e) {
         throw new RuntimeException(e);
      }
      return sbResponseBody.toString();
   }

   /**
    * <div>
    *    <p>
    *       <div>
    *          This function generated a shuffled array with size {@code size} that is received as
    *          a parameters. The Array is filled with integer values from 1 to {@code size} <b>without
    *          duplicates, each integer has only 1 occurrence</b>.
    *       </div>
    *       <div>
    *          <ul>For example:
    *             <li>For size = 5 the array can be [ 4, 2, 1, 5, 3 ]</li>
    *             <li>For size = 10 the array can be [ 8, 5, 1, 9, 2, 6, 7, 3, 4, 10 ]
    *          </ul>
    *       </div>
    *    </p>
    *    <p>
    *       <div>The method's complexity is O(n):</div>
    *       <ul>
    *          <li>Generating the {@link ArrayList} and populating it - O(n)</li>
    *          <li>Method {@link #shuffle} to shuffle the values in the {@link ArrayList} - O(n)</li>
    *          <li>Method {@link #swap} to swap an element in {@link ArrayList} randomly with another element, element - O(1) x 4.</li>
    *       </ul>
    *    </p>
    * </div>
    * @param size The size of the shuffled array, and the high-limit of the range of integers populating the array.
    * @return {@link Integer}[]. Shuffled array of Integers populated with values from 1 to {@code size} without duplicates.
    */
   @Override
   public Integer[] generateShuffledArray(int size) {
      List<Integer> listIntegers = new ArrayList<>();
      for (int i = 0; i < size; i++) {
         listIntegers.add(i+1);
      }
      return shuffle(listIntegers);
   }

   @Override
   public String pingService() {
      return "Shuffle service is running and ready to process requests.";
   }

   private Integer[] shuffle(List<Integer> list) {
      for (int i=list.size(); i>1; i--) {
         int rnd = RND.nextInt(i);
         list = swap(list, i - 1, rnd);
      }
      return list.toArray(new Integer[list.size()]);
   }

   private List<Integer> swap(List<Integer> list, int i, int j) {
      Integer tmp = list.get(i);
      list.set(i, list.get(j));
      list.set(j, tmp);
      return list;
   }

   private HttpRequest generateLogMessagePostRequest(String message) {
      String strLogServiceBaseUrl = logServiceProtocol + "://" + logServiceHost + ":" + logServicePort + "/" + logServicePath;
      HttpRequest request = HttpRequest.newBuilder()
              .uri(URI.create(strLogServiceBaseUrl + "/v1/shuffledArray"))
              .POST(HttpRequest.BodyPublishers.ofString("{\"logLevel\":\"INFO\", \"message\": \""  + message + "\"}"))
              .build();
      return request;
   }
}

package com.beniregev.serviceshuffle.service.impl;

import com.beniregev.serviceshuffle.service.ShuffleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


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
   public Map<String, Object> generateAndLogShuffledArray(int size) {
      Integer[] arrIntegers = generateShuffledArray(size);
      if (arrIntegers.length < 1) {
         throw new ArrayStoreException("Shuffled integers array length is " + arrIntegers.length);
      }
      HttpRequest request = generateLogMessagePostRequest("Sync", "Shuffled array with " + size + " elements: " + Arrays.toString(arrIntegers));
      StringBuffer sbResponseBody;
      Map<String, Object> mapBody = null;
      try {
         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         if (response.statusCode()/100 != 2) {
            sbResponseBody = new StringBuffer("Calling POST requests FAILED.")
                    .append("\nHTTP Status code: ")
                    .append(response.statusCode())
                    .append("\nResponse Body: \n")
                    .append(response.body());
         } else {
            mapBody = getResponseBodyAsMap(response.body());
            System.out.println("Response body received back in service-shuffle: " + mapBody);
            sbResponseBody = new StringBuffer("Calling POST requests SUCCESSFUL.")
                    .append("\nHTTP Status code: ")
                    .append(response.statusCode())
                    .append("\ntimestamp:")
                    .append(mapBody.get("timestamp").toString())
                    .append("\nRequest sent ")
                    .append(mapBody.get("sending").toString())
                    .append("\nlogLevel: ")
                    .append(mapBody.get("logLevel").toString())
                    .append("\nmessage text: \n")
                    .append(mapBody.get("message").toString());
         }
      } catch (IOException | InterruptedException e) {
         throw new RuntimeException(e);
      }

      // Instead of blocking our code, we can sent the request asynchronously using sendAsync method.
      // This method will immediately return a CompletableFuture instance.
      try {
         request = generateLogMessagePostRequest("Async", "Shuffled array with " + size + " elements: " + Arrays.toString(arrIntegers));
         // Same request sent asynchronously using the sendAsync method
         CompletableFuture<HttpResponse<String>> futureResponse = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
         // The CompletableFuture completes with the HttpResponse once it becomes available
         HttpResponse<String> response = futureResponse.get();
         if (response.statusCode() / 100 != 2) {
            sbResponseBody = new StringBuffer("Calling POST requests FAILED.")
                    .append("\nHTTP Status code: ")
                    .append(response.statusCode())
                    .append("\nResponse Body: \n")
                    .append(response.body());
         } else {
            mapBody = getResponseBodyAsMap(response.body());
            System.out.println("Response body received back in service-shuffle: " + mapBody);
            sbResponseBody = new StringBuffer("Calling POST requests SUCCESSFUL.")
                    .append("\nHTTP Status code: ")
                    .append(response.statusCode())
                    .append("\ntimestamp:")
                    .append(mapBody.get("timestamp").toString())
                    .append("\nRequest sent ")
                    .append(mapBody.get("sending").toString())
                    .append("\nlogLevel: ")
                    .append(mapBody.get("logLevel").toString())
                    .append("\nmessage text: \n")
                    .append(mapBody.get("message").toString());
         }
      } catch (InterruptedException e) {
         throw new RuntimeException(e);
      } catch (ExecutionException e) {
         throw new RuntimeException(e);
      }
      return mapBody;
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

   /**
    * Return a String to acknowledge the code reached and received by the service.
    * @return a String to acknowledge the code reached and received by the service
    */
   @Override
   public String pingService() {
      return "Shuffle service is running and ready to process requests.";
   }

   private Integer[] shuffle(List<Integer> list) {
      for (int i=list.size(); i>1; i--) {
         int rnd = RND.nextInt(i);
         swap(list, i - 1, rnd);
      }
      return list.toArray(new Integer[list.size()]);
   }

   private List<Integer> swap(List<Integer> list, int i, int j) {
      Integer tmp = list.get(i);
      list.set(i, list.get(j));
      list.set(j, tmp);
      return list;
   }

   /**
    * <div>
    *    <p>
    *       I know that the correct way to get the base-URL for service-log is
    *       by getting the values from system/environment variables.
    *    </p>
    *    <p>
    *       <div>
    *          I just wanted to demonstrate at least one technique that I know how
    *          to put the values in a ".properties" file, get them, and set them into
    *          properties of a class.
    *       </div>
    *       <div>
    *          Of course there are other ways, such as using {@link System#getenv(String)}
    *          method, and others.
    *       </div>
    *    </p>
    * </div>
    * @param message
    * @return {@link HttpRequest} generated using {@link HttpClient} from Java 11 to create
    * the REST API request to be sent to "service-log".
    */
   private HttpRequest generateLogMessagePostRequest(String sending, String message) {
      String strLogServiceBaseUrl = String.format("%s://%s:%s/%s", logServiceProtocol, logServiceHost, logServicePort, logServicePath);
      return HttpRequest.newBuilder()
              .uri(URI.create(strLogServiceBaseUrl + "/v1/shuffledArray"))
              .POST(HttpRequest.BodyPublishers.ofString("{\"sending\": \"" + sending + "\", \"logLevel\":\"INFO\", \"message\": \""  + message + "\"}"))
              .build();
   }

   private Map<String, Object> getResponseBodyAsMap(String responseBody) {
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Object> map;
      try {
         map = mapper.readValue(responseBody, new TypeReference<>() {});
      } catch (JsonProcessingException e) {
         throw new RuntimeException(e);
      }
      return map;
   }
}

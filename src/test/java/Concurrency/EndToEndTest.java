package Concurrency;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EndToEndTest {
  @Test
  public void EndToEnd() throws InterruptedException {
    ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();
    users.put("88000000000", new User("Dwayne", "Johnson", "88000000000"));

    List<Message> results = new CopyOnWriteArrayList<>();
    HashMapUserRepository repository = new HashMapUserRepository(users);
    EnrichmentService enrichmentService = new EnrichmentService(List.of(new MsisdnStrategy(repository)));
    ExecutorService executorService = Executors.newFixedThreadPool(2);
    CountDownLatch latch = new CountDownLatch(2);

    Map<String, String> firstContent = new HashMap<>();
    firstContent.put("msisdn", "88000000000");
    Map<String, String> secondContent = new HashMap<>();
    secondContent.put("msisdn", "88111111111");
    List<Message> message = List.of(
            new Message(firstContent, EnrichmentType.MSISDN),
            new Message(secondContent, EnrichmentType.MSISDN)
    );

    for (int i = 0; i < 2; i++) {
      final int finalI = i;
      executorService.submit(() -> {
        results.add(enrichmentService.enrich(message.get(finalI)));
        latch.countDown();
      });

//      latch.await();
    }

    for (Message result : results) {
      Map<String, String> content = result.getContent();
      if (content.get("msisdn").equals("88111111111")) {
        assertFalse(content.containsKey("firstName"));
      } else {
        assertTrue(content.containsKey("firstName"));
      }
    }
  }
}

package Concurrency;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EnrichmentServiceTest {

  @Test
  void enrich() {
    EnrichmentService enrichmentService = new EnrichmentService(List.of(new EnrichmentStrategy() {
      @Override
      public EnrichmentType getType() {
        return EnrichmentType.MSISDN;
      }

      @Override
      public Message enrich(Message message) {
        return new Message(new HashMap<>(), EnrichmentType.MSISDN);
      }
    }));
    HashMap<String, String> content = new HashMap<>();
    content.put("msisdn", "88000000000");
    Message message = new Message(content,EnrichmentType.MSISDN);
    Message result = enrichmentService.enrich(message);

    assertNotEquals(message, result);
  }
}
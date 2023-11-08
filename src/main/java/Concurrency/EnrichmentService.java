package Concurrency;

import java.util.List;

public class EnrichmentService {
  private final UserRepository repository;
  List<EnrichmentStrategy> strategies;

  public EnrichmentService(UserRepository repository, List<EnrichmentStrategy> strategies) {
    this.repository = repository;
    this.strategies = strategies;
  }

  public Message enrich(Message message) {
    for (var strategy : strategies) {
      if (strategy.getType() == message.enrichmentType) {
        return strategy.enrich(message);
      }
    }
    return message;
  }
}

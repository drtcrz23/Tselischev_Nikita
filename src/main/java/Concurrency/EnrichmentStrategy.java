package Concurrency;

public interface EnrichmentStrategy {
  EnrichmentType getType();
  Message enrich(Message message);
}

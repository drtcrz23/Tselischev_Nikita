package Concurrency;

import java.util.HashMap;

public class MsisdnStrategy implements EnrichmentStrategy {
  private final UserRepository repository;

  public MsisdnStrategy(UserRepository repository) {
    this.repository = repository;
  }

  @Override
  public EnrichmentType getType() {
    return EnrichmentType.MSISDN;
  }

  @Override
  public Message enrich(Message message) {
    User user = repository.findByMsisdn(message.content.get("msisdn"));

    var copyOfContent = new HashMap<String, String>();
    for (String key : message.content.keySet()) {
      copyOfContent.put(key, message.content.get(key));
    }
    copyOfContent.put("firstName", user.getFirstName());
    copyOfContent.put("lastName", user.getLastName());
    Message copyOfMessage = new Message(copyOfContent, message.enrichmentType);
    return copyOfMessage;
  }
}

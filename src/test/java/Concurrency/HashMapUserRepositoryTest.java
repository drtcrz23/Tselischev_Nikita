package Concurrency;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class HashMapUserRepositoryTest {
  @Test
  void findByMsisdn() {
    ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    users.put("88000000000", new User("Jason", "Statham", "88000000000"));

    HashMapUserRepository repo = new HashMapUserRepository(users);

    assertNotNull(repo.findByMsisdn("88000000000"));
  }
}
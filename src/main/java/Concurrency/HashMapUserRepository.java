package Concurrency;

import java.util.concurrent.ConcurrentHashMap;

public class HashMapUserRepository implements UserRepository {
  private final ConcurrentHashMap<String, User> user;

  public HashMapUserRepository(ConcurrentHashMap<String, User> user) {
    this.user = user;
  }

  @Override
  public User findByMsisdn(String numberPhone) {
    return user.get(numberPhone);
  }
}

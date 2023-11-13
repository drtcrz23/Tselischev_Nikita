package Concurrency;

import java.util.concurrent.ConcurrentHashMap;

public class HashMapUserRepository implements UserRepository {
  private final ConcurrentHashMap<String, User> users;

  public HashMapUserRepository(ConcurrentHashMap<String, User> users) {
    this.users = users;
  }
  @Override
  public User findByMsisdn(String numberPhone) {
    return users.get(numberPhone);
  }

  @Override
  public void updateUserByMsisdn(String msisdn, User user) {
    users.put(msisdn, user);
  }

}

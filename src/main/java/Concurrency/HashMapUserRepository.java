package Concurrency;

import java.util.*;

public class Repository implements UserRepository{

//  public Map<String, String> enrich(Map<String, String> input) {
//    Map<String, String> result = new HashMap<>(input);
//    result.put("firstName", "Vasya");
//    result.put("lastName", "Ivanov");
//    return result;
//  }

  private final HashMap<String, User> user = new HashMap<>();
//  @Override
//  public Optional<User> findUser(String numberPhone) {
//    return Optional.empty();
//  }

  @Override
  public User findByMsisdn(String msisdn) {
    return UserRepository.Users.get(msisdn);
  }
}

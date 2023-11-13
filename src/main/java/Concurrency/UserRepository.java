package Concurrency;

public interface UserRepository {
  User findByMsisdn(String numberPhone);
//  void updateUserByMsisdn(String msisdn, User user);
}

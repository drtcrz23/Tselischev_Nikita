package Concurrency;

public interface UserRepository {
  User findByMsisdn(String numberPhone);
}

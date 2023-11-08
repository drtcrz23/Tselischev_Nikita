package Concurrency;

public class User {
  private final String firstName;
  private final String lastName;
  private final String numberPhone;

  public User(String firstName, String lastName, String numberPhone) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.numberPhone = numberPhone;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getNumberPhone() {
    return numberPhone;
  }
}

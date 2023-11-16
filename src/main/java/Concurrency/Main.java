package Concurrency;

import java.util.HashMap;
public class Main {
  public static void main(String[] args) {
    HashMap<String, String> Message = new HashMap<>();
    Message.put("action", "button_click");
    Message.put("page", "book_card");
    Message.put("msisdn", "88005553535");
    System.out.println(Message);
  }
}

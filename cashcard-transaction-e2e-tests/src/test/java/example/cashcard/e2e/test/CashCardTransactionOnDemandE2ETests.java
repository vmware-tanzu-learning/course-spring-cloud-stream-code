package example.cashcard.e2e.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CashCardTransactionOnDemandE2ETests {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  // Enable this annotation when we write the test!
  // @Test
  void cashCardTransactionOnDemandEndToEnd() throws IOException {
  }

}

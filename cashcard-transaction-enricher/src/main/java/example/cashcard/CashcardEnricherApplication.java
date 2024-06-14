package example.cashcard;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CashcardEnricherApplication {

	public static void main(String[] args) throws JsonProcessingException {
		SpringApplication.run(CashcardEnricherApplication.class, args);
	}

}

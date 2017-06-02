package gs.nysub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = {"me.ramswaroop.jbot", "io.pivotal"})
public class NysubSlackbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(NysubSlackbotApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}

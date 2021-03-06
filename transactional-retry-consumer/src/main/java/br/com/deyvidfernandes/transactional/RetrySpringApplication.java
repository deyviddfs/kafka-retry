package br.com.deyvidfernandes.transactional;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan("br.com.deyvidfernandes.transactional")
@EnableScheduling
public class RetrySpringApplication {
	public static void main(String[] args) {
		org.springframework.boot.SpringApplication.run(RetrySpringApplication.class, args);
	}
}

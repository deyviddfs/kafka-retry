package br.com.deyvidfernandes.transactional;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan("br.com.deyvidfernandes.transactional")
public class SpringApplication {
	public static void main(String[] args) {
		org.springframework.boot.SpringApplication.run(SpringApplication.class, args);
	}
}

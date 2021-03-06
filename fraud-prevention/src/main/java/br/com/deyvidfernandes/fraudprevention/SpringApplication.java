package br.com.deyvidfernandes.fraudprevention;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class SpringApplication {

	public static void main(String[] args) {
		org.springframework.boot.SpringApplication.run(SpringApplication.class, args);
	}

}

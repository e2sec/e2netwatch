package de.e2security.e2netwatch.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"de.e2security.e2netwatch.web",
		"de.e2security.e2netwatch.usermanagement",
		"de.e2security.e2netwatch.security"
		})
public class Application {

    public static void main(String[] args) {
    	SpringApplication.run(Application.class, args);
    }

}

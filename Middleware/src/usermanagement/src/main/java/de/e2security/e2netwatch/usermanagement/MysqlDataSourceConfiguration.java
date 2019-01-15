package de.e2security.e2netwatch.usermanagement;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import de.e2security.e2netwatch.usermanagement.security.encoder.Argon2PasswordEncoder;

@Configuration
@PropertySource({ "classpath:mysql/persistence-${envTarget:dev}.properties"})
@EnableAutoConfiguration
@EnableTransactionManagement
public class MysqlDataSourceConfiguration {
	
	@Bean
	public PasswordEncoder argonPasswordEncoder() {
		return new Argon2PasswordEncoder();
	}

}

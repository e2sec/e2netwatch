package de.e2security.e2netwatch.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import de.e2security.e2netwatch.usermanagement.security.encoder.Argon2PasswordEncoder;

@Configuration
@EnableResourceServer
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@ComponentScan({ "de.e2security.e2netwatch.usermanagement.service" })
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    public ResourceServerConfiguration() {
        super();
    }
    
    @Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new Argon2PasswordEncoder();
		return encoder;
	}

    // global security concerns

    @Bean
    public AuthenticationProvider authProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider());
    }

    // http security concerns

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        // @formatter:off
        http.
	        authorizeRequests().
	        anyRequest().authenticated().and().
	        sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().
	        csrf().disable();
	        // @formatter:on
    }

}

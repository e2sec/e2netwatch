package de.e2security.e2netwatch.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import de.e2security.e2netwatch.spring.security.CustomAccessDeniedHandler;
import de.e2security.e2netwatch.spring.security.JWTAuthenticationFilter;
import de.e2security.e2netwatch.spring.security.JWTAuthorizationFilter;
import de.e2security.e2netwatch.usermanagement.security.encoder.Argon2PasswordEncoder;
import de.e2security.e2netwatch.usermanagement.service.CustomUserDetailsService;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {
	
	@Value("${secret}")
	private String SECRET;
	@Value("${expiration_time}")
	private long EXPIRATION_TIME;
	@Value("${header_string}")
	private String HEADER_STRING;
	@Value("${token_prefix}")
	private String TOKEN_PREFIX;
    
    @Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new Argon2PasswordEncoder();
		return encoder;
	}
    private CustomUserDetailsService userDetailsService;

    public WebSecurity(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    
    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
        		.antMatchers(HttpMethod.GET, "/api/public/welcome").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), SECRET, EXPIRATION_TIME))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), HEADER_STRING,SECRET, TOKEN_PREFIX))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
    
    @Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}
	
}

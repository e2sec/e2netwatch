package de.e2security.e2netwatch.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
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

import de.e2security.e2netwatch.security.handler.CustomAccessDeniedHandler;
import de.e2security.e2netwatch.security.jwt.JWTAuthenticationFilter;
import de.e2security.e2netwatch.security.jwt.JWTAuthorizationFilter;
import de.e2security.e2netwatch.security.userdetails.CustomUserDetailsService;
import de.e2security.e2netwatch.usermanagement.security.encoder.Argon2PasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@PropertySource({ "classpath:jwt/security-${envTarget:dev}.properties"})
public class WebSecurity extends WebSecurityConfigurerAdapter {
	
	@Value("${jwt.secret}")
	private String SECRET;
	@Value("${jwt.expiration_time}")
	private long EXPIRATION_TIME;
	@Value("${jwt.header_string}")
	private String HEADER_STRING;
	@Value("${jwt.token_prefix}")
	private String TOKEN_PREFIX;
	@Value("${security.login_path:/auth/login}")
	private String LOGIN_PATH;
    
    @Bean
	public PasswordEncoder passwordEncoder() {
    	return new Argon2PasswordEncoder();
	}
    
    @Autowired
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
        		.antMatchers(HttpMethod.GET, "/public/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), SECRET, EXPIRATION_TIME, LOGIN_PATH))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), HEADER_STRING, SECRET, TOKEN_PREFIX))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
    
    /**
     * CORS configuration
     */
    @Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}
	
}
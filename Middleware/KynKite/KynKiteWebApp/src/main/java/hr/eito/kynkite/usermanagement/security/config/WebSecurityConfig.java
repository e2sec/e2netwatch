
/*
    Copyright (C) 2017 e-ito Technology Services GmbH
    e-mail: info@e-ito.de
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/


package hr.eito.kynkite.usermanagement.security.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import hr.eito.kynkite.usermanagement.security.encoder.Argon2PasswordEncoder;
import hr.eito.kynkite.usermanagement.security.jwt.auth.AuthenticationFailureHandler;
import hr.eito.kynkite.usermanagement.security.jwt.auth.AuthenticationSuccessHandler;
import hr.eito.kynkite.usermanagement.security.jwt.auth.JwtLogoutHandler;
import hr.eito.kynkite.usermanagement.security.jwt.auth.RestAuthenticationEntryPoint;
import hr.eito.kynkite.usermanagement.security.jwt.auth.TokenAuthenticationFilter;
 
/**
 * Spring security configuration bean
 * 
 * @author Hrvoje
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
     
    @Bean
    public TokenAuthenticationFilter jwtAuthenticationTokenFilter() throws Exception {
        return new TokenAuthenticationFilter();
    }
    
    @Bean
    public JwtLogoutHandler jwtLogoutHandler() {
        return new JwtLogoutHandler();
    }
    
    @Autowired
    RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    
    @Autowired
    DataSource dataSource;
     
    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
    	auth.jdbcAuthentication().dataSource(dataSource)
		.usersByUsernameQuery(
			"select username, password, 1 from user where username=?")
		.authoritiesByUsernameQuery(
				new StringBuilder()
				.append("select usr.username, aty.name ")
				.append("from user usr join usr_aty uay on uay.usr_id = usr.id ")
				.append("join authority aty on aty.id = uay.aty_id ")
				.append("where usr.username=? ")
				.append("union select usr.username, aty.name ")
				.append("from user usr join usr_ugr uug on uug.usr_id = usr.id ")
				.append("join user_group ugr on ugr.id = uug.ugr_id ")
				.append("join ugr_aty uga on uga.ugr_id = ugr.id ")
				.append("join authority aty on aty.id = uga.aty_id")
				.toString())
		.passwordEncoder(passwordEncoder());
    }
    
    @Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new Argon2PasswordEncoder();
		return encoder;
	}
    
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
     
    @Override
    protected void configure(HttpSecurity http) throws Exception {
  
    	http
        .sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS ).and()
        .exceptionHandling().authenticationEntryPoint( restAuthenticationEntryPoint ).and()
        .addFilterBefore(jwtAuthenticationTokenFilter(), BasicAuthenticationFilter.class)
        .authorizeRequests()
            .antMatchers().permitAll()
          .anyRequest()
            .authenticated().and()
        .formLogin()
        	.loginProcessingUrl("/rest/login")
            .successHandler(authenticationSuccessHandler)
            .failureHandler(authenticationFailureHandler).and()
        .logout()
        	.logoutUrl("/rest/logout")
            .addLogoutHandler(jwtLogoutHandler())
            .logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)));
    	
    	http.csrf().disable();
    	
    }
}

package de.e2security.e2netwatch.security.jwt;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.e2security.e2netwatch.utils.constants.MyCustomError;

/**
 * JWT authentication filter
 * 
 * @author Hrvoje
 *
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private String SECRET;
	private long EXPIRATION_TIME;
	
    private AuthenticationManager authenticationManager;

    /**
     * Constructor for JWTAuthenticationFilter
     * 
     * @param authenticationManager
     * @param secret Passphrase to build JWT with
     * @param expirationTime How long is the created JWT valid in seconds
     */
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, 
    		final String secret, final long expirationTime, final String loginPath) {
    	setFilterProcessesUrl(loginPath);
        this.authenticationManager = authenticationManager;
        this.SECRET = secret;
        this.EXPIRATION_TIME = expirationTime;
    }

    /**
     * Authentication method that does prepare steps for default 
     * authentication manager authenticate method.
     * 
     * <p>Parses json request body to extract username and password</p>
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) 
    		throws AuthenticationException {
		try {
			String jsonAsString = IOUtils.toString(req.getInputStream(), "UTF-8");
			JSONObject json = new JSONObject(jsonAsString);
			
			String username = (String)json.get("username");
			String password = (String)json.get("password");
			
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
						username,
						password,
						new ArrayList<>()
					)
			);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (JSONException jsonException) {
			try {
				badRequestBody(res, jsonException);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return null;
		}
	}
    
    /**
     * Behaviour on successful authentication
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, 
    		FilterChain chain, Authentication auth) throws IOException, ServletException {

    	ObjectMapper mapper = new ObjectMapper();
    	
    	// Get roles from authenticated user
    	List<String> roles = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    	String[] roleStringsAsArray = roles.toArray(new String[0]);
    	
    	// Create JWT token based on some user data
        String token = JWT.create()
                .withSubject(((User) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME*1000))
                .withArrayClaim("roles", roleStringsAsArray)
                .sign(HMAC512(SECRET.getBytes()));
        
        // Create response body object with: token and expiration time
        AuthOKResponse authOKResponse = new AuthOKResponse(token, EXPIRATION_TIME, roles.get(0));
        
        // Update response based on created JWT data
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write(mapper.writeValueAsString(authOKResponse));
    }
    
    /**
     * Behaviour on unsuccessful authentication
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res, 
    		AuthenticationException failed) throws IOException {
    	
    	ObjectMapper mapper = new ObjectMapper();
    	
    	// Creating response body object with message
    	MyCustomError error = new MyCustomError(
    			HttpStatus.UNAUTHORIZED.value(), 
    			HttpStatus.UNAUTHORIZED.getReasonPhrase(), 
    			failed.getMessage());
    	
    	// Update response with JSON body and HTTP status
        res.setContentType("application/json");
        res.getWriter().write(mapper.writeValueAsString(error));
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
    
    /**
     * Method to handle bad request for authentication
     * 
     * @param res
     * @param failed
     * @throws IOException
     */
    private void badRequestBody(HttpServletResponse res, JSONException failed) 
    		throws IOException {
    	
    	ObjectMapper mapper = new ObjectMapper();
    	
    	// Creating response body object with message
    	MyCustomError error = new MyCustomError(
    			HttpStatus.BAD_REQUEST.value(), 
    			HttpStatus.BAD_REQUEST.getReasonPhrase(), 
    			"Authentication request was malformed");
    	
    	// Update response with JSON body and HTTP status
        res.setContentType("application/json");
        res.getWriter().write(mapper.writeValueAsString(error));
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
    
}

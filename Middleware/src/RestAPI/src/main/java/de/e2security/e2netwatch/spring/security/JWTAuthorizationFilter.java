package de.e2security.e2netwatch.spring.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class that encapsulates JWT Authorization process
 * 
 * @author Hrvoje
 *
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	
	@Value("${header_string}")
	private String HEADER_STRING;
	@Value("${secret}")
	private String SECRET;
	@Value("${token_prefix}")
	private String TOKEN_PREFIX;

	/**
	 * Constructor for JWTAuthorizationFilter
	 * 
	 * @param authManager
	 * @param headerString
	 * @param secret
	 * @param tokenPrefix
	 */
    public JWTAuthorizationFilter(AuthenticationManager authManager, final String headerString, final String secret, final String tokenPrefix) {
        super(authManager);
        this.HEADER_STRING = headerString;
        this.SECRET = secret;
        this.TOKEN_PREFIX = tokenPrefix;
    }

    /**
     * Filter incoming request
     */
    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        try {
	    	String header = req.getHeader(HEADER_STRING);
	
	        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
	            chain.doFilter(req, res);
	            return;
	        }
	
	        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
	
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        chain.doFilter(req, res);
        } catch (JWTVerificationException e) {
        	SecurityContextHolder.clearContext();
			onUnsuccessfulAuthentication(req, res, e);
			return;
        }
    }
    
    /**
     * Modify response on invalid JWT verification
     * 
     * @param req
     * @param res
     * @param failed
     * @throws IOException
     */
    protected void onUnsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res, JWTVerificationException failed)
					throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
    	AuthErrorResponse jwtUnsuccessfulResponse = new AuthErrorResponse("unsuccessful_authentication", failed.getMessage());
    	
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write(mapper.writeValueAsString(jwtUnsuccessfulResponse));
        res.setStatus(HttpStatus.FORBIDDEN.value());
	}

    /**
     * Get spring.security object with all user data inside
     * 
     * @param request Servlet request with JWT token
     * @return object with all user data from token
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            // Verify the token and decode
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""));
            
            // Get username from decoded token
            String user = decodedJWT.getSubject();
            
            // Get list of roles from token
            List<String> listOfRoles = decodedJWT.getClaim("roles").asList(String.class);
            List<GrantedAuthority> authorities = listOfRoles.stream()
                    .map(authority -> new SimpleGrantedAuthority(authority))
                    .collect(Collectors.toList());

            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, authorities);
            }
            return null;
        }
        return null;
    }
}

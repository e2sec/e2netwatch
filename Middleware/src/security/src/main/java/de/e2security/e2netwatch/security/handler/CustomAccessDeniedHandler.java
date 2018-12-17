package de.e2security.e2netwatch.security.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.e2security.e2netwatch.utils.constants.MyCustomError;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
 
    @Override
    public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException accessDeniedException) 
    		throws IOException {
    	
    	ObjectMapper mapper = new ObjectMapper();
    	
    	// Creating response body object with message
    	MyCustomError error = new MyCustomError(
    			HttpStatus.FORBIDDEN.value(), 
    			HttpStatus.FORBIDDEN.getReasonPhrase(), 
    			accessDeniedException.getMessage());
    	
    	// Update response with JSON body and HTTP status
        res.setContentType("application/json");
        res.getWriter().write(mapper.writeValueAsString(error));
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
//            LOG.warn("User: " + auth.getName() 
//              + " attempted to access the protected URL: "
//              + req.getRequestURI());
        }
    }
}

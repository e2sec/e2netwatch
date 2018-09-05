package de.e2security.e2netwatch.spring.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	 
    public static final Logger LOG
      = Logger.getLogger(CustomAccessDeniedHandler.class);
 
    @Override
    public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException accessDeniedException) 
    		throws IOException, ServletException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            LOG.warn("User: " + auth.getName() 
              + " attempted to access the protected URL: "
              + req.getRequestURI());
        }
        ObjectMapper mapper = new ObjectMapper();
    	AuthErrorResponse authErrorResponse = new AuthErrorResponse("access_denied", accessDeniedException.getMessage());
    	
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write(mapper.writeValueAsString(authErrorResponse));
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}

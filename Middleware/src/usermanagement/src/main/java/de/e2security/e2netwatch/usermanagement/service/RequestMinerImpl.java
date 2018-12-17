package de.e2security.e2netwatch.usermanagement.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Implementation of RequestMiner interface
 * 
 * @author Hrvoje
 *
 */
@Service
public class RequestMinerImpl implements RequestMiner {
	
	/*
	 * (non-Javadoc)
	 * @see de.e2security.e2netwatch.usermanagement.service.RequestMiner#getCurrentUsername()
	 */
	@Override
	public String getCurrentUsername() {
		final SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext == null) {
            return null;
        }
        final Authentication authentication = securityContext.getAuthentication();
        
        if (authentication == null) {
            return null;
        }

        return authentication.getName();
	}
	
}

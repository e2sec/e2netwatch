package de.e2security.e2netwatch.usermanagement.security.encoder;

import org.springframework.security.crypto.password.PasswordEncoder;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

/**
 * Argon2i implementation of Password encoder needed to encode and verify user passwords
 * 
 * @author Hrvoje
 */
public class Argon2PasswordEncoder implements PasswordEncoder {
	
	private final static int N = 65536;
	private final static int r = 2;
	private final static int p = 1;
	
	Argon2 argon2;
	
	public Argon2PasswordEncoder() {
		argon2 = Argon2Factory.create();
	}

	@Override
	public String encode(CharSequence rawPassword) {
		return argon2.hash(r, N, p, rawPassword.toString());
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
	    if (argon2.verify(encodedPassword, rawPassword.toString())) {
	        return true;
	    } else {
	        return false;
	    }
	}
	
}

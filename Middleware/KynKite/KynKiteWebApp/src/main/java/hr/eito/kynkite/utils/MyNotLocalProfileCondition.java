package hr.eito.kynkite.utils;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class MyNotLocalProfileCondition implements Condition {
	
	private static final String EXCLUDING_PROFILE = "local";

	@Override
	public boolean matches(ConditionContext context,
			AnnotatedTypeMetadata metadata) {
		if (context.getEnvironment() != null) {
			final String activeProfiles = context.getEnvironment().getProperty("spring.profiles.active");
			if (activeProfiles.contains(EXCLUDING_PROFILE)) {
				return false;
			}
        }
        return true;
	}

}

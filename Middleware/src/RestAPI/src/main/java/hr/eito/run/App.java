package hr.eito.run;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import hr.eito.spring.AqlJpaConfig;
import hr.eito.spring.AuthorizationServerConfiguration;
import hr.eito.spring.UserJpaConfig;
import hr.eito.spring.setup.MyApplicationContextInitializer;
import hr.eito.spring.MvcConfig;
import hr.eito.spring.PropertiesConfig;
import hr.eito.spring.ResourceServerConfiguration;
import hr.eito.spring.ServiceConfig;
import hr.eito.spring.ServletConfig;

@SpringBootApplication(exclude = {
		ErrorMvcAutoConfiguration.class
})
public class App extends SpringBootServletInitializer {
	
	private final static Object[] CONFIGS = {
			UserJpaConfig.class
			, AqlJpaConfig.class
			, ServiceConfig.class
			, MvcConfig.class
			, PropertiesConfig.class
			, ServletConfig.class
			
			, App.class
			
			, ResourceServerConfiguration.class
			, AuthorizationServerConfiguration.class
    };

    //
	
	@Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(CONFIGS).initializers(new MyApplicationContextInitializer());
    }

    public static void main(final String... args) {
        final SpringApplication springApplication = new SpringApplication(CONFIGS);
        springApplication.addInitializers(new MyApplicationContextInitializer());
        springApplication.run(args);
    }

}

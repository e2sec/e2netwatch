package de.e2security.e2netwatch.spring;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource({ "classpath:persistence-${envTarget:dev}.properties" })
@ComponentScan(basePackages = { "de.e2security.e2netwatch.usermanagement" })
@EnableJpaRepositories(basePackages = "de.e2security.e2netwatch.usermanagement.dao")
@EnableTransactionManagement
public class UserJpaConfig {
	
	@Autowired
    private Environment env;
	
	public UserJpaConfig() {
        super();
	}
	
	// beans

    @Bean(name="userEntityManager")
    @Primary
    public LocalContainerEntityManagerFactoryBean userEntityManager() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(userDataSource());
        em.setPackagesToScan(new String[] { "de.e2security.e2netwatch.usermanagement.model" });
        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());
        return em;
    }

    @Bean
    @Primary
    public DataSource userDataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("jdbc.driver"));
        dataSource.setUrl(env.getProperty("jdbc.um.url"));
        dataSource.setUsername(env.getProperty("jdbc.um.username"));
        dataSource.setPassword(env.getProperty("jdbc.um.password"));
        return dataSource;
    }

    @Bean(name="userTransactionManager")
    @Primary
    public JpaTransactionManager userTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(userEntityManager().getObject());
        return transactionManager;
    }

    @Bean
    @Primary
    public PersistenceExceptionTranslationPostProcessor userExceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    //

    final Properties additionalProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        hibernateProperties.setProperty("hibernate.id.new_generator_mappings", env.getProperty("hibernate.id.new_generator_mappings"));
        hibernateProperties.setProperty("hibernate.show_sql", "true");
        return hibernateProperties;
    }
    
}

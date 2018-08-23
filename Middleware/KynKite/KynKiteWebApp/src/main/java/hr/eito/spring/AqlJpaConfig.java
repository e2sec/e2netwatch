package hr.eito.spring;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
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
@ComponentScan(basePackages = { "hr.eito.kynkite.aql" })
@EnableJpaRepositories(basePackages = "hr.eito.kynkite.aql.dao")
@EnableTransactionManagement
public class AqlJpaConfig {
	
	@Autowired
    private Environment env;
	
	public AqlJpaConfig() {
        super();
	}
	
	// beans

    @Bean(name="aqlEntityManager")
    public LocalContainerEntityManagerFactoryBean aqlEntityManager() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(aqlDataSource());
        em.setPackagesToScan(new String[] { "hr.eito.kynkite.aql.model" });
        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());
        return em;
    }

    @Bean
    public DataSource aqlDataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("jdbc.driver"));
        dataSource.setUrl(env.getProperty("jdbc.aql.url"));
        dataSource.setUsername(env.getProperty("jdbc.aql.username"));
        dataSource.setPassword(env.getProperty("jdbc.aql.password"));
        return dataSource;
    }

    @Bean(name="aqlTransactionManager")
    public JpaTransactionManager aqlTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(aqlEntityManager().getObject());
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor aqlExceptionTranslation() {
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

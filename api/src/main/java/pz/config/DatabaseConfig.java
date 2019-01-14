package pz.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import static pz.config.DatabaseConfig.REPOSITORY_PACKAGE_REFERENCE_PATH;


@Configuration
@EnableJpaRepositories(value = REPOSITORY_PACKAGE_REFERENCE_PATH)
public class DatabaseConfig {
    public static final String ENTITY_PACKAGE_REFERENCE_PATH = "pz.model.database.entities";
    public static final String REPOSITORY_PACKAGE_REFERENCE_PATH = "pz.model.database.repositories";
    @Autowired
    private JpaVendorAdapter jpaVendorAdapter;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "liquibase")
    public SpringLiquibase configure() {
        SpringLiquibase springLiquibase = new SpringLiquibase();
        springLiquibase.setChangeLog("classpath:liquibase-changelog.xml");
        springLiquibase.setDataSource(dataSource());
        springLiquibase.setShouldRun(true);

        return springLiquibase;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setPackagesToScan(ENTITY_PACKAGE_REFERENCE_PATH);
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactoryBean.afterPropertiesSet();

        return entityManagerFactoryBean.getObject();
    }
}

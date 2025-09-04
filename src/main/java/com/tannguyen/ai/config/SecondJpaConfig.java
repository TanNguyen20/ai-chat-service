package com.tannguyen.ai.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.tannguyen.ai.repository.secondary",
        entityManagerFactoryRef = "secondEntityManagerFactory",
        transactionManagerRef = "secondTransactionManager"
)
public class SecondJpaConfig {
    @Bean
    @ConfigurationProperties("datasources.secondary")
    public HikariDataSource secondDataSource() {
        return new HikariDataSource();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean secondEntityManagerFactory(
            @Qualifier("secondDataSource") DataSource ds, EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(ds)
                .packages(
                        "com.tannguyen.ai.model.secondary",
                        "com.tannguyen.ai.model.audit"
                )
                .persistenceUnit("secondary")
                .build();
    }

    @Bean
    public PlatformTransactionManager secondTransactionManager(
            @Qualifier("secondEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
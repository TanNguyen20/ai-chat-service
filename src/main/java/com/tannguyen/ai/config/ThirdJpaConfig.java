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
        basePackages = "com.tannguyen.ai.repository.third",
        entityManagerFactoryRef = "thirdEntityManagerFactory",
        transactionManagerRef = "thirdTransactionManager"
)
public class ThirdJpaConfig {
    @Bean
    @ConfigurationProperties("datasources.third")
    public HikariDataSource thirdDataSource() {
        return new HikariDataSource();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean thirdEntityManagerFactory(
            @Qualifier("thirdDataSource") DataSource ds, EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(ds)
                .packages(
                        "com.tannguyen.ai.model.third",
                        "com.tannguyen.ai.model.audit"
                )
                .persistenceUnit("third")
                .build();
    }

    @Bean
    public PlatformTransactionManager thirdTransactionManager(
            @Qualifier("thirdEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
package com.example.crmsystem.config;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class JdbiConfig {
    @Bean
    public Jdbi jdbi(DataSource dataSource) {
        Jdbi jdbi = Jdbi.create(dataSource);

        jdbi.installPlugin(new SqlObjectPlugin());

        try (java.sql.Connection conn = dataSource.getConnection()) {
            String databaseName = conn.getMetaData().getDatabaseProductName();

            if ("PostgreSQL".equalsIgnoreCase(databaseName)) {
                jdbi.installPlugin(new org.jdbi.v3.postgres.PostgresPlugin());
            }
        } catch (Exception e) {
            // Лог
        }

        return jdbi;
    }
}
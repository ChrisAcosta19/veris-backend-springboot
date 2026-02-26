package com.veris.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class OracleDataSourceConfig {

    @Bean
    public DataSource dataSource() {
        // Leer variables de entorno (priorizando .env si existe)
        io.github.cdimascio.dotenv.Dotenv dotenv = io.github.cdimascio.dotenv.Dotenv.configure().ignoreIfMissing()
                .load();

        String dbHost = dotenv.get("DB_HOST", System.getenv().getOrDefault("DB_HOST", "localhost"));
        String dbPort = dotenv.get("DB_PORT", System.getenv().getOrDefault("DB_PORT", "1521"));
        String dbServiceName = dotenv.get("DB_SERVICE_NAME", System.getenv("DB_SERVICE_NAME"));
        String dbSid = dotenv.get("DB_SID", System.getenv("DB_SID"));
        String dbUser = dotenv.get("DB_USER", System.getenv().getOrDefault("DB_USER", "system"));
        String dbPassword = dotenv.get("DB_PASSWORD", System.getenv().getOrDefault("DB_PASSWORD", "oracle"));

        // Construir la URL de conexión
        String jdbcUrl;
        if (dbServiceName != null && !dbServiceName.isEmpty()) {
            // Sintaxis Easy Connect para CloudClusters: host:port/service_name
            jdbcUrl = String.format("jdbc:oracle:thin:@%s:%s/%s", dbHost, dbPort, dbServiceName);
            log.info("Oracle DataSource configured with SERVICE_NAME: {}", dbServiceName);
        } else if (dbSid != null && !dbSid.isEmpty()) {
            // Sintaxis SID para conexiones locales: host:port:sid
            jdbcUrl = String.format("jdbc:oracle:thin:@%s:%s:%s", dbHost, dbPort, dbSid);
            log.info("Oracle DataSource configured with SID: {}", dbSid);
        } else {
            // Default a SID si no se proporciona ninguno
            jdbcUrl = String.format("jdbc:oracle:thin:@%s:%s:ORCLCDB", dbHost, dbPort);
            log.warn("No DB_SERVICE_NAME or DB_SID provided, using default ORCLCDB SID");
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(dbUser);
        config.setPassword(dbPassword);
        config.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setAutoCommit(true);

        log.info("Creating HikariCP DataSource with URL: {}", jdbcUrl.replaceAll(":[^:]*@", ":***@")); // Log sin
                                                                                                       // mostrar
                                                                                                       // password

        return new HikariDataSource(config);
    }
}

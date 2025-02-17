package se.callista.blog.service.multitenancy.config.tenant;

import javax.sql.DataSource;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import se.callista.blog.service.multitenancy.config.tenant.liquibase.DynamicSchemaBasedMultiTenantSpringLiquibase;
import se.callista.blog.service.multitenancy.repository.TenantRepository;

@Lazy(false)
//@Configuration
@ConditionalOnProperty(name = "multitenancy.tenant.liquibase.enabled", havingValue = "true", matchIfMissing = true)
public class TenantLiquibaseConfig {

    @Bean
    @ConfigurationProperties("multitenancy.tenant.liquibase")
    public SpringLiquibase tenantLiquibaseProperties() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:db/changelog/db.changelog-tenant.yaml");
        return liquibase;
    }

    @Bean
    public DynamicSchemaBasedMultiTenantSpringLiquibase tenantLiquibase(
        TenantRepository masterTenantRepository,
        DataSource dataSource,
        @Qualifier("tenantLiquibaseProperties")
        SpringLiquibase liquibaseProperties) {
        return new DynamicSchemaBasedMultiTenantSpringLiquibase(masterTenantRepository, dataSource, liquibaseProperties);
    }

}

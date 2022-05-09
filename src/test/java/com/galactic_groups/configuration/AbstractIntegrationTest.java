package com.galactic_groups.configuration;

import com.galactic_groups.utils.MultiAuthorizationRequestHelperFactory;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration(
        initializers = PostgresInitializer.class
)
@Sql("/sql/data.sql")
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AbstractIntegrationTest {
    // TODO: сделать тесты попроще

    @Autowired
    protected MultiAuthorizationRequestHelperFactory multiAuthorizationRequestHelperFactory;

    @BeforeAll
    static void initContainer() {
        PostgresInitializer.container.start();
    }
}

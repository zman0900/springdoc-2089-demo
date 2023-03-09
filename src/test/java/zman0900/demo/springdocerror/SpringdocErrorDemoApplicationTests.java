package zman0900.demo.springdocerror;

import static org.assertj.core.api.Assertions.STRING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringdocErrorDemoApplicationTests {
    @Value(value = "${local.server.port}")
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testOpenApi() {
        assertThat(restTemplate.getForEntity("http://localhost:" + port + "/v3/api-docs", String.class))
				.as("returns 200 status")
                .returns(200, from(ResponseEntity::getStatusCodeValue))
				.as("returns non-empty body")
                .extracting(ResponseEntity::getBody, STRING)
                .isNotEmpty();
    }

}

package com.mmacedoaraujo.avaliacaojavaspringdedalus.integration;

import com.mmacedoaraujo.avaliacaojavaspringdedalus.domain.User;
import com.mmacedoaraujo.avaliacaojavaspringdedalus.repository.UserRepository;
import com.mmacedoaraujo.avaliacaojavaspringdedalus.util.UserCreator;
import com.mmacedoaraujo.avaliacaojavaspringdedalus.wrapper.PageableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class UserControllerIntegrationTests {

    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplateRoleUser;
    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdmin")
    private TestRestTemplate testRestTemplateRoleAdmin;
    @Autowired
    private UserRepository repository;

    @TestConfiguration
    @Lazy
    static class Config {
        @Bean(name = "testRestTemplateRoleUser")
        public TestRestTemplate testRestTemplateUser(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateUser = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("user", "user");

            return new TestRestTemplate(restTemplateUser);
        }

        @Bean(name = "testRestTemplateRoleAdmin")
        public TestRestTemplate testRestTemplateAdmin(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateAdmin = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("admin", "admin");

            return new TestRestTemplate(restTemplateAdmin);
        }
    }

    @Test
    void returnUsersRegistered() {
        repository.save(UserCreator.createUserWithId("Teste", "Integração"));
        ResponseEntity<List<User>> usersPageRoleAdmin = testRestTemplateRoleAdmin.exchange("/api/v1/users", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<User>>() {
                });

        assertNotNull(usersPageRoleAdmin.getBody());
        assertInstanceOf(List.class, usersPageRoleAdmin.getBody());
        assertInstanceOf(User.class, usersPageRoleAdmin.getBody().get(0));
        assertEquals(1, usersPageRoleAdmin.getBody().size());
        assertEquals("Teste", usersPageRoleAdmin.getBody().get(0).getName());
        assertEquals("Integração", usersPageRoleAdmin.getBody().get(0).getLastName());
        assertEquals(1L, usersPageRoleAdmin.getBody().get(0).getId());
        assertEquals(HttpStatus.OK, usersPageRoleAdmin.getStatusCode());

    }

    @Test
    void returnUsersRegisteredPageable() {
        repository.save(UserCreator.createUserWithId("Teste", "Integração"));
        ResponseEntity<PageableResponse<User>> usersPageRoleUser = testRestTemplateRoleUser.exchange("/api/v1/users/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<User>>() {
                });

        assertNotNull(usersPageRoleUser.getBody().toList());
        assertInstanceOf(Page.class, usersPageRoleUser.getBody());
        assertInstanceOf(User.class, usersPageRoleUser.getBody().toList().get(0));
        assertEquals(1, usersPageRoleUser.getBody().toList().size());
        assertEquals("Teste", usersPageRoleUser.getBody().toList().get(0).getName());
        assertEquals("Integração", usersPageRoleUser.getBody().toList().get(0).getLastName());
        assertEquals(1L, usersPageRoleUser.getBody().toList().get(0).getId());
        assertEquals(HttpStatus.OK, usersPageRoleUser.getStatusCode());

        ResponseEntity<PageableResponse<User>> usersPageRoleAdmin = testRestTemplateRoleAdmin.exchange("/api/v1/users/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<User>>() {
                });

        assertNotNull(usersPageRoleAdmin.getBody().toList());
        assertInstanceOf(Page.class, usersPageRoleAdmin.getBody());
        assertInstanceOf(User.class, usersPageRoleAdmin.getBody().toList().get(0));
        assertEquals(1, usersPageRoleAdmin.getBody().toList().size());
        assertEquals("Teste", usersPageRoleAdmin.getBody().toList().get(0).getName());
        assertEquals("Integração", usersPageRoleAdmin.getBody().toList().get(0).getLastName());
        assertEquals(1L, usersPageRoleAdmin.getBody().toList().get(0).getId());
        assertEquals(HttpStatus.OK, usersPageRoleAdmin.getStatusCode());
    }

}

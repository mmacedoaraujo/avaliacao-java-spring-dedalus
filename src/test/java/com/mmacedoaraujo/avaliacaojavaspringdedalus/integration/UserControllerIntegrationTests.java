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
    @Qualifier(value = "testRestTemplateRoleAdmin")
    private TestRestTemplate testRestTemplateRoleAdmin;
    @Autowired
    @Qualifier(value = "testRestTemplateNoAuth")
    private TestRestTemplate testRestTemplateNoAuth;
    @Autowired
    private UserRepository repository;

    @TestConfiguration
    @Lazy
    static class Config {
        @Bean(name = "testRestTemplateRoleAdmin")
        public TestRestTemplate testRestTemplateAdmin(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateAdmin = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("admin", "admin");

            return new TestRestTemplate(restTemplateAdmin);
        }

        @Bean(name = "testRestTemplateNoAuth")
        public TestRestTemplate testRestTemplateNoAuth(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateNoAuth = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port);

            return new TestRestTemplate(restTemplateNoAuth);
        }
    }

    @Test
    void returnUsersRegistered() {
        User userForComparison = UserCreator.createUserWithId("Teste", "Integração");
        repository.save(UserCreator.createUserWithId("Teste", "Integração"));

        ResponseEntity<List<User>> usersPageNoAuth = testRestTemplateNoAuth.exchange("/api/v1/users", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<User>>() {
                });

        assertNotNull(usersPageNoAuth.getBody());
        assertInstanceOf(List.class, usersPageNoAuth.getBody());
        assertInstanceOf(User.class, usersPageNoAuth.getBody().get(0));
        assertEquals(1, usersPageNoAuth.getBody().size());
        assertEquals(userForComparison.getName(), usersPageNoAuth.getBody().get(0).getName());
        assertEquals(userForComparison.getLastName(), usersPageNoAuth.getBody().get(0).getLastName());
        assertEquals(userForComparison.getId(), usersPageNoAuth.getBody().get(0).getId());
        assertEquals(HttpStatus.OK, usersPageNoAuth.getStatusCode());

        ResponseEntity<List<User>> usersPageRoleAdmin = testRestTemplateRoleAdmin.exchange("/api/v1/users", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<User>>() {
                });

        assertNotNull(usersPageRoleAdmin.getBody());
        assertInstanceOf(List.class, usersPageRoleAdmin.getBody());
        assertInstanceOf(User.class, usersPageRoleAdmin.getBody().get(0));
        assertEquals(1, usersPageRoleAdmin.getBody().size());
        assertEquals(userForComparison.getName(), usersPageRoleAdmin.getBody().get(0).getName());
        assertEquals(userForComparison.getLastName(), usersPageRoleAdmin.getBody().get(0).getLastName());
        assertEquals(userForComparison.getId(), usersPageRoleAdmin.getBody().get(0).getId());
        assertEquals(HttpStatus.OK, usersPageRoleAdmin.getStatusCode());

    }

    @Test
    void returnUsersRegisteredPageable() {
        User userForComparison = UserCreator.createUserWithId("Teste", "Integração");
        repository.save(UserCreator.createUserWithId("Teste", "Integração"));
        ResponseEntity<PageableResponse<User>> usersPageRoleUser = testRestTemplateNoAuth.exchange("/api/v1/users/paginated", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<User>>() {
                });

        assertNotNull(usersPageRoleUser.getBody().toList());
        assertInstanceOf(Page.class, usersPageRoleUser.getBody());
        assertInstanceOf(User.class, usersPageRoleUser.getBody().toList().get(0));
        assertEquals(1, usersPageRoleUser.getBody().toList().size());
        assertEquals(userForComparison.getName(), usersPageRoleUser.getBody().toList().get(0).getName());
        assertEquals(userForComparison.getLastName(), usersPageRoleUser.getBody().toList().get(0).getLastName());
        assertEquals(userForComparison.getId(), usersPageRoleUser.getBody().toList().get(0).getId());
        assertEquals(HttpStatus.OK, usersPageRoleUser.getStatusCode());

        ResponseEntity<PageableResponse<User>> usersPageRoleAdmin = testRestTemplateRoleAdmin.exchange("/api/v1/users/paginated", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<User>>() {
                });

        assertNotNull(usersPageRoleAdmin.getBody().toList());
        assertInstanceOf(Page.class, usersPageRoleAdmin.getBody());
        assertInstanceOf(User.class, usersPageRoleAdmin.getBody().toList().get(0));
        assertEquals(1, usersPageRoleAdmin.getBody().toList().size());
        assertEquals(userForComparison.getName(), usersPageRoleAdmin.getBody().toList().get(0).getName());
        assertEquals(userForComparison.getLastName(), usersPageRoleAdmin.getBody().toList().get(0).getLastName());
        assertEquals(userForComparison.getId(), usersPageRoleAdmin.getBody().toList().get(0).getId());
        assertEquals(HttpStatus.OK, usersPageRoleAdmin.getStatusCode());
    }

    @Test
    void returnUserByIdWithoutBasicAuth() {
        repository.save(UserCreator.createUserWithId("Teste", "Integração"));

        ResponseEntity<User> userByIdNoAuth = testRestTemplateNoAuth.exchange("/api/v1/users/1", HttpMethod.GET, null,
                new ParameterizedTypeReference<User>() {
                });

        assertEquals(HttpStatus.UNAUTHORIZED, userByIdNoAuth.getStatusCode());
        assertEquals(null, userByIdNoAuth.getBody());
    }

    @Test
    void returnUserById() {
        User userForComparison = UserCreator.createUserWithId("Teste", "Integração");
        repository.save(UserCreator.createUserWithId("Teste", "Integração"));

        ResponseEntity<User> userByIdRoleAdmin = testRestTemplateRoleAdmin.exchange("/api/v1/users/1", HttpMethod.GET, null,
                new ParameterizedTypeReference<User>() {
                });

        assertNotNull(userByIdRoleAdmin.getBody());
        assertInstanceOf(User.class, userByIdRoleAdmin.getBody());
        assertEquals(userForComparison.getName(), userByIdRoleAdmin.getBody().getName());
        assertEquals(userForComparison.getLastName(), userByIdRoleAdmin.getBody().getLastName());
        assertEquals(userForComparison.getBirthDate(), userByIdRoleAdmin.getBody().getBirthDate());
        assertEquals(userForComparison.getId(), userByIdRoleAdmin.getBody().getId());
        assertEquals(HttpStatus.OK, userByIdRoleAdmin.getStatusCode());
    }

    @Test
    void addNewUserWithoutBasicAuth() {
        User userForComparison = UserCreator.createUserWithId("Teste", "Integração");

        ResponseEntity<User> returnFromPostRequestWithoutBasicAuth = testRestTemplateNoAuth.postForEntity("/api/v1/users/addNewUser", userForComparison, User.class);

        assertEquals(HttpStatus.UNAUTHORIZED, returnFromPostRequestWithoutBasicAuth.getStatusCode());
        assertEquals(null, returnFromPostRequestWithoutBasicAuth.getBody());
    }

    @Test
    void addNewUserWithAdminCredentials() {
        User userForComparison = UserCreator.createUserWithId("Teste", "Integração");

        ResponseEntity<User> returnFromPostRequestWithAdminCredentials = testRestTemplateRoleAdmin.postForEntity("/api/v1/users/addNewUser", userForComparison, User.class);

        assertNotNull(returnFromPostRequestWithAdminCredentials.getBody());
        assertInstanceOf(User.class, returnFromPostRequestWithAdminCredentials.getBody());
        assertEquals(userForComparison.getName(), returnFromPostRequestWithAdminCredentials.getBody().getName());
        assertEquals(userForComparison.getLastName(), returnFromPostRequestWithAdminCredentials.getBody().getLastName());
        assertEquals(userForComparison.getId(), returnFromPostRequestWithAdminCredentials.getBody().getId());
        assertEquals(HttpStatus.CREATED, returnFromPostRequestWithAdminCredentials.getStatusCode());
    }



}

package com.mmacedoaraujo.userapi.integration;

import com.mmacedoaraujo.userapi.domain.User;
import com.mmacedoaraujo.userapi.repository.UserRepository;
import com.mmacedoaraujo.userapi.util.UserCreator;
import com.mmacedoaraujo.userapi.wrapper.PageableResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerIntegrationTest {

    @Autowired
    @Qualifier(value = "testRestTemplate")
    private TestRestTemplate testRestTemplate;
    @Autowired
    @Qualifier(value = "testRestTemplateRabbitMQ")
    private TestRestTemplate testRestTemplateRabbitMQ;
    @Autowired
    private UserRepository repository;

    @Mock
    private RabbitTemplate template;

    @TestConfiguration
    @Lazy
    static class Config {

        @Bean(name = "testRestTemplateRabbitMQ")
        public TestRestTemplate testRestTemplateRabbitMQ() {
            RestTemplateBuilder restTemplate = new RestTemplateBuilder()
                    .rootUri("http://localhost:8081")
                    .basicAuthentication("admin", "admin");

            return new TestRestTemplate(restTemplate);
        }

        @Bean(name = "testRestTemplate")
        public TestRestTemplate testRestTemplate(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplate = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port);

            return new TestRestTemplate(restTemplate);
        }
    }

    @Test
    void returnUsersRegistered() {
        User userForComparison = UserCreator.createUserWithId("Teste", "Integração");
        repository.save(UserCreator.createUserWithId("Teste", "Integração"));

        ResponseEntity<List<User>> usersPage = testRestTemplate.exchange("/api/v1/users", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<User>>() {
                });

        assertNotNull(usersPage.getBody());
        assertInstanceOf(List.class, usersPage.getBody());
        assertInstanceOf(User.class, usersPage.getBody().get(0));
        assertEquals(1, usersPage.getBody().size());
        assertEquals(userForComparison.getName(), usersPage.getBody().get(0).getName());
        assertEquals(userForComparison.getLastName(), usersPage.getBody().get(0).getLastName());
        assertEquals(userForComparison.getId(), usersPage.getBody().get(0).getId());
        assertEquals(HttpStatus.OK, usersPage.getStatusCode());

    }

    @Test
    void returnUsersRegisteredPageable() {
        User userForComparison = UserCreator.createUserWithId("Teste", "Integração");
        repository.save(UserCreator.createUserWithId("Teste", "Integração"));

        ResponseEntity<PageableResponse<User>> usersPage = testRestTemplate.exchange("/api/v1/users/paginated", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<User>>() {
                });

        assertNotNull(usersPage.getBody().toList());
        assertInstanceOf(Page.class, usersPage.getBody());
        assertInstanceOf(User.class, usersPage.getBody().toList().get(0));
        assertEquals(1, usersPage.getBody().toList().size());
        assertEquals(userForComparison.getName(), usersPage.getBody().toList().get(0).getName());
        assertEquals(userForComparison.getLastName(), usersPage.getBody().toList().get(0).getLastName());
        assertEquals(userForComparison.getId(), usersPage.getBody().toList().get(0).getId());
        assertEquals(HttpStatus.OK, usersPage.getStatusCode());
    }

    @Test
    void returnUserById() {
        User userForComparison = UserCreator.createUserWithId("Teste", "Integração");
        repository.save(UserCreator.createUserWithId("Teste", "Integração"));

        ResponseEntity<User> userFoundOnDatabaseById = testRestTemplate.exchange("/api/v1/users/1", HttpMethod.GET, null,
                new ParameterizedTypeReference<User>() {
                });

        assertNotNull(userFoundOnDatabaseById.getBody());
        assertInstanceOf(User.class, userFoundOnDatabaseById.getBody());
        assertEquals(userForComparison.getName(), userFoundOnDatabaseById.getBody().getName());
        assertEquals(userForComparison.getLastName(), userFoundOnDatabaseById.getBody().getLastName());
        assertEquals(userForComparison.getBirthDate(), userFoundOnDatabaseById.getBody().getBirthDate());
        assertEquals(userForComparison.getId(), userFoundOnDatabaseById.getBody().getId());
        assertEquals(HttpStatus.OK, userFoundOnDatabaseById.getStatusCode());
    }

    @Test
    void removeUser() {
        User userAtDatabase = repository.save(UserCreator.createUserWithId("Teste", "Integração"));

        ResponseEntity<Void> returnFromPutRequest = testRestTemplate.exchange("/api/v1/users/deleteUser/1",
                HttpMethod.DELETE,
                null,
                Void.class,
                userAtDatabase.getId());

        assertEquals(HttpStatus.NO_CONTENT, returnFromPutRequest.getStatusCode());

    }

    @Test
    void addNewUser() {
        User userForComparison = UserCreator.createUserWithId("Teste", "Integração");

        ResponseEntity<User> returnFromPostRequest =
                testRestTemplateRabbitMQ.exchange("/api/v1/users/addNewUser", HttpMethod.POST, new HttpEntity<>(userForComparison), User.class);

        assertNotNull(returnFromPostRequest.getBody());
        assertInstanceOf(User.class, returnFromPostRequest.getBody());
        assertEquals(userForComparison.getName(), returnFromPostRequest.getBody().getName());
        assertEquals(userForComparison.getLastName(), returnFromPostRequest.getBody().getLastName());
        assertNotNull(returnFromPostRequest.getBody().getId());
        assertEquals(HttpStatus.CREATED, returnFromPostRequest.getStatusCode());
    }

    @Test
    void updateUser() {
        User userWithModifications = UserCreator.createUserWithId("Integration", "Test");
        repository.save(UserCreator.createUserWithId("Teste", "Integração"));

        ResponseEntity<Void> returnFromPutRequest = testRestTemplate.exchange("/api/v1/users/updateUser/1",
                HttpMethod.PUT,
                new HttpEntity<>(userWithModifications),
                Void.class);

        assertEquals(HttpStatus.NO_CONTENT, returnFromPutRequest.getStatusCode());
    }

}

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
    @Qualifier(value = "testRestTemplateRoleAdmin")
    private TestRestTemplate testRestTemplateRoleAdmin;
    @Autowired
    @Qualifier(value = "testRestTemplateNoAuth")
    private TestRestTemplate testRestTemplateNoAuth;
    @Autowired
    @Qualifier(value = "testRestTemplateAdminRabbitMQ")
    private TestRestTemplate testRestTemplateAdminRabbitMQ;
    @Autowired
    private UserRepository repository;

    @Mock
    private RabbitTemplate template;

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

        @Bean(name = "testRestTemplateAdminRabbitMQ")
        public TestRestTemplate testRestTemplateAdminRabbitMQ() {
            RestTemplateBuilder restTemplateAdmin = new RestTemplateBuilder()
                    .rootUri("http://localhost:8081")
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
    void returnUsersRegisteredWithoutCredentials() {
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

    }

    @Test
    void returnUsersRegisteredWithAdminCredentials() {
        User userForComparison = UserCreator.createUserWithId("Teste", "Integração");
        repository.save(UserCreator.createUserWithId("Teste", "Integração"));

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
    void returnUsersRegisteredPageableWithoutCredentials() {
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
    }

    @Test
    void returnUsersRegisteredPageableWithAdminCredentials() {
        User userForComparison = UserCreator.createUserWithId("Teste", "Integração");
        repository.save(UserCreator.createUserWithId("Teste", "Integração"));

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
        assertNull(userByIdNoAuth.getBody());
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
        assertNull(returnFromPostRequestWithoutBasicAuth.getBody());
    }


    @Test
    void updateUserDataWithoutCredentials() {
        User userWithModifications = UserCreator.createUserWithId("Integration", "Test");
        repository.save(UserCreator.createUserWithId("Teste", "Integração"));

        ResponseEntity<Void> returnFromPutRequestWithoutCredentials = testRestTemplateNoAuth.exchange("/api/v1/users/updateUser/1",
                HttpMethod.PUT,
                new HttpEntity<>(userWithModifications),
                Void.class);

        assertEquals(HttpStatus.UNAUTHORIZED, returnFromPutRequestWithoutCredentials.getStatusCode());
        assertNull(returnFromPutRequestWithoutCredentials.getBody());

    }


    @Test
    void removeUserWithoutCredentials() {
        User userAtDatabase = repository.save(UserCreator.createUserWithId("Teste", "Integração"));

        ResponseEntity<Void> returnFromPutRequestWithoutCredentials = testRestTemplateNoAuth.exchange("/api/v1/users/deleteUser/1",
                HttpMethod.DELETE,
                null,
                Void.class,
                userAtDatabase.getId());

        assertEquals(HttpStatus.UNAUTHORIZED, returnFromPutRequestWithoutCredentials.getStatusCode());
        assertNull(returnFromPutRequestWithoutCredentials.getBody());

    }

    @Test
    void removeUserWithAdminCredentials() {
        User userAtDatabase = repository.save(UserCreator.createUserWithId("Teste", "Integração"));

        ResponseEntity<Void> returnFromPutRequestWithoutCredentials = testRestTemplateRoleAdmin.exchange("/api/v1/users/deleteUser/1",
                HttpMethod.DELETE,
                null,
                Void.class,
                userAtDatabase.getId());

        assertEquals(HttpStatus.NO_CONTENT, returnFromPutRequestWithoutCredentials.getStatusCode());

    }

//    @Test
//    void addNewUserWithAdminCredentials() {
//        User userForComparison = UserCreator.createUserWithId("Teste", "Integração");
//
//        ResponseEntity<User> returnFromPostRequestWithAdminCredentials =
//                testRestTemplateAdminRabbitMQ.exchange("/api/v1/users/addNewUser", HttpMethod.POST, new HttpEntity<>(userForComparison), User.class);
//
//        assertNotNull(returnFromPostRequestWithAdminCredentials.getBody());
//        assertInstanceOf(User.class, returnFromPostRequestWithAdminCredentials.getBody());
//        assertEquals(userForComparison.getName(), returnFromPostRequestWithAdminCredentials.getBody().getName());
//        assertEquals(userForComparison.getLastName(), returnFromPostRequestWithAdminCredentials.getBody().getLastName());
//        assertNotNull(returnFromPostRequestWithAdminCredentials.getBody().getId());
//        assertEquals(HttpStatus.CREATED, returnFromPostRequestWithAdminCredentials.getStatusCode());
//    }

    @Test
    void updateUserDataWithAdminCredentials() {
        User userWithModifications = UserCreator.createUserWithId("Integration", "Test");
        repository.save(UserCreator.createUserWithId("Teste", "Integração"));

        ResponseEntity<Void> returnFromPutRequestWithoutCredentials = testRestTemplateRoleAdmin.exchange("/api/v1/users/updateUser/1",
                HttpMethod.PUT,
                new HttpEntity<>(userWithModifications),
                Void.class);

        assertEquals(HttpStatus.NO_CONTENT, returnFromPutRequestWithoutCredentials.getStatusCode());
    }

}

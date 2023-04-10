package com.mmacedoaraujo.userapi.controller;

import com.mmacedoaraujo.userapi.domain.User;
import com.mmacedoaraujo.userapi.service.serviceimpl.UserServiceImpl;
import com.mmacedoaraujo.userapi.util.UserCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController controller;

    @Mock
    private UserServiceImpl service;

    @Mock
    private RabbitTemplate template;

    @Test
    void returnsHello() {

        ResponseEntity<String> stringResponseEntity = controller.returnsHello();

        assertNotNull(stringResponseEntity);
        assertEquals("Hello World!", stringResponseEntity.getBody());
        assertEquals(HttpStatus.OK, stringResponseEntity.getStatusCode());
    }

    @Test
    @DisplayName(value = "returnsUsersRegistered() returns a list of users when successfull ")
    void returnUsersRegistered() {
        List<User> listOfUsers = UserCreator.createListOfUsers();

        when(service.returnAllUsers()).thenReturn(listOfUsers);

        ResponseEntity<List<User>> listOfUsersFromController = controller.returnUsersRegistered();

        assertNotNull(listOfUsersFromController);
        assertInstanceOf(User.class, listOfUsersFromController.getBody().get(0));
        assertEquals(3, listOfUsersFromController.getBody().size());
        assertEquals("João", listOfUsersFromController.getBody().get(0).getName());
        assertEquals(1L, listOfUsersFromController.getBody().get(0).getId());
        assertEquals(HttpStatus.OK, listOfUsersFromController.getStatusCode());
    }

    @Test
    @DisplayName(value = "returnUsersRegisteredPageable() returns a list of users inside a page object when successfull ")
    void returnUsersRegisteredPageable() {

        List<User> listOfUsers = UserCreator.createListOfUsers();
        Page<User> pageOfUsers = new PageImpl<>(listOfUsers);
        Pageable pageable = Pageable.unpaged();

        when(service.returnAllUsersPageable(pageable)).thenReturn(pageOfUsers);

        ResponseEntity<Page<User>> usersPageFromController = controller.returnUsersRegisteredPageable(pageable);

        assertNotNull(usersPageFromController.getBody());
        assertInstanceOf(User.class, usersPageFromController.getBody().toList().get(0));
        assertEquals(3, usersPageFromController.getBody().toList().size());
        assertEquals("João", usersPageFromController.getBody().toList().get(0).getName());
        assertEquals(1L, usersPageFromController.getBody().toList().get(0).getId());
        assertEquals(HttpStatus.OK, usersPageFromController.getStatusCode());
    }

    @Test
    @DisplayName(value = "returnUserById() returns a valid user when successfull ")
    void returnUserById() {
        User user = UserCreator.createUserWithId("Marcos", "Teste");

        when(service.findUserById(ArgumentMatchers.anyLong())).thenReturn(user);

        ResponseEntity<User> userByIdFromController = controller.returnUserById(1L);

        assertNotNull(userByIdFromController.getBody());
        assertInstanceOf(User.class, userByIdFromController.getBody());
        assertEquals("Marcos", userByIdFromController.getBody().getName());
        assertEquals("Teste", userByIdFromController.getBody().getLastName());
        assertEquals(1L, userByIdFromController.getBody().getId());
        assertEquals(HttpStatus.OK, userByIdFromController.getStatusCode());
    }

    @Test
    @DisplayName(value = "addNewUser() returns a valid user when successfull ")
    void addNewUser() {
        User newUser = UserCreator.createUserWithId("Teste", "Teste");

        when(service.saveNewUser(ArgumentMatchers.any(User.class))).thenReturn(newUser);

        ResponseEntity<User> userSavedFromController = controller.addNewUser(newUser);

        assertNotNull(userSavedFromController.getBody());
        assertInstanceOf(User.class, userSavedFromController.getBody());
        assertEquals("Teste", userSavedFromController.getBody().getName());
        assertEquals("Teste", userSavedFromController.getBody().getLastName());
        assertEquals(1L, userSavedFromController.getBody().getId());
        assertEquals(HttpStatus.CREATED, userSavedFromController.getStatusCode());
    }

    @Test
    @DisplayName(value = "updateUserData() updates a valid user when successfull ")
    void updateUserData() {
        User user = UserCreator.createUserWithId("Teste", "Teste");

        ResponseEntity<Void> response = controller.updateUserData(user, user.getId());

        verify(service, times(1)).updateUser(user, user.getId());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName(value = "removeUser() deletes a user when successfull ")
    void removeUser() {
        User user = UserCreator.createUserWithId("Teste", "Teste");

        ResponseEntity<Void> response = controller.removeUser(user.getId());

        verify(service, times(1)).deleteUser(user.getId());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
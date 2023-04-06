package com.mmacedoaraujo.avaliacaojavaspringdedalus.controller;

import com.mmacedoaraujo.avaliacaojavaspringdedalus.domain.User;
import com.mmacedoaraujo.avaliacaojavaspringdedalus.service.serviceimpl.UserServiceImpl;
import com.mmacedoaraujo.avaliacaojavaspringdedalus.util.UserCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    @Test
    void returnsHello() {

        ResponseEntity<String> stringResponseEntity = controller.returnsHello();

        assertNotNull(stringResponseEntity);
        assertEquals("Hello World!", stringResponseEntity.getBody());
        assertEquals(HttpStatus.OK, stringResponseEntity.getStatusCode());
    }

    @Test
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
    void updateUserData() {
        User user = UserCreator.createUserWithId("Teste", "Teste");

        ResponseEntity<Void> response = controller.updateUserData(user, user.getId());

        verify(service, times(1)).updateUser(user, user.getId());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void removeUser() {
        User user = UserCreator.createUserWithId("Teste", "Teste");

        ResponseEntity<Void> response = controller.removeUser(user.getId());

        verify(service, times(1)).deleteUser(user.getId());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
package com.mmacedoaraujo.avaliacaojavaspringdedalus.service.serviceimpl;

import com.mmacedoaraujo.avaliacaojavaspringdedalus.domain.User;
import com.mmacedoaraujo.avaliacaojavaspringdedalus.exceptions.UserNotFoundException;
import com.mmacedoaraujo.avaliacaojavaspringdedalus.repository.UserRepository;
import com.mmacedoaraujo.avaliacaojavaspringdedalus.util.UserCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldReturnAllUsers() {
        //Creating a list of users
        List<User> usersList = UserCreator.createListOfUsers();

        when(repository.findAll()).thenReturn(usersList);

        List<User> usersFromService = userService.returnAllUsers();

        assertNotNull(usersFromService);
        assertEquals(3, usersFromService.size());
        assertEquals(usersList, usersFromService);
        assertEquals("João", usersFromService.get(0).getName());
        assertEquals(1L, usersFromService.get(0).getId());


    }

    @Test
    public void testReturnAllUsersPageable() {
        List<User> users = UserCreator.createListOfUsers();
        Page<User> page = new PageImpl<>(users);
        Pageable pageable = Pageable.unpaged();

        when(repository.findAll(pageable)).thenReturn(page);

        Page<User> usersPage = userService.returnAllUsersPageable(pageable);

        assertNotNull(usersPage);
        assertEquals(page, usersPage);
        assertEquals(3, usersPage.getSize());
        assertEquals("João", usersPage.toList().get(0).getName());
        assertEquals(1L, usersPage.toList().get(0).getId());


    }

    @Test
    public void testFindUserById() {
        User user = UserCreator.createUserWithId("Teste", "Teste");

        when(repository.findById(1L)).thenReturn(Optional.of(user));

        User userFromService = userService.findUserById(1L);

        assertNotNull(userFromService);
        assertEquals(user, userFromService);
        assertEquals("Teste", userFromService.getName());
        assertEquals(1L, userFromService.getId());
    }

    @Test
    public void testFindUserByIdNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.findUserById(1L));
    }

    @Test
    public void testSaveNewUser() {
        User userToBeSaved = UserCreator.createUserWithId("João", "Teste");
        when(repository.save(userToBeSaved)).thenReturn(userToBeSaved);

        // When
        User savedUser = userService.saveNewUser(userToBeSaved);

        // Then
        verify(repository, times(1)).save(userToBeSaved);
        assertNotNull(savedUser);
        assertEquals(userToBeSaved.getId(), savedUser.getId());
        assertEquals(userToBeSaved.getName(), savedUser.getName());
        assertEquals(userToBeSaved.getBirthDate(), savedUser.getBirthDate());
    }

    @Test
    void updateUser() {
        User user = UserCreator.createUserWithId("João", "Teste");

        when(repository.findById(1L)).thenReturn(Optional.of(user));

        userService.updateUser(user, 1L);

        verify(repository, times(1)).save(user);
    }

    @Test
    void deleteUser() {
        User user = UserCreator.createUserWithId("João", "Teste");

        when(repository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(repository, times(1)).deleteById(1L);
    }
}
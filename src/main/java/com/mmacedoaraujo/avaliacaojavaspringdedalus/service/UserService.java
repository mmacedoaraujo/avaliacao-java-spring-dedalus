package com.mmacedoaraujo.avaliacaojavaspringdedalus.service;

import com.mmacedoaraujo.avaliacaojavaspringdedalus.domain.User;

import java.util.List;

public interface UserService {

    List<User> returnAllUsers();

    User findUserById(Long id);

    Long saveNewUser(User user);

    void updateUser(User user, Long id);

    void deleteUser(Long id);

}

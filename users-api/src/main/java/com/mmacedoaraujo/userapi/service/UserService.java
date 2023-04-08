package com.mmacedoaraujo.userapi.service;

import com.mmacedoaraujo.userapi.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    List<User> returnAllUsers();

    Page<User> returnAllUsersPageable(Pageable pageable);

    User findUserById(Long id);

    User saveNewUser(User user);

    void updateUser(User user, Long id);

    void deleteUser(Long id);

}

package com.mmacedoaraujo.avaliacaojavaspringdedalus.controller;

import com.mmacedoaraujo.avaliacaojavaspringdedalus.domain.User;
import com.mmacedoaraujo.avaliacaojavaspringdedalus.service.serviceimpl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserServiceImpl service;

    @GetMapping
    public ResponseEntity<List<User>> returnUsersRegistered() {
        List<User> users = service.returnAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<User>> returnUsersRegisteredPageable(Pageable pageable) {
        Page<User> usersPage = service.returnAllUsersPageable(pageable);
        return new ResponseEntity<>(usersPage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> returnUserById(@PathVariable Long id) {
        User userFoundById = service.findUserById(id);

        return new ResponseEntity<>(userFoundById, HttpStatus.OK);
    }

    @PostMapping("/addNewUser")
    public ResponseEntity<Long> addNewUser(@RequestBody User user) {
        Long newUserId = service.saveNewUser(user);

        return new ResponseEntity<>(newUserId, HttpStatus.CREATED);
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<Void> updateUserData(@RequestBody User user, @PathVariable Long id) {
        service.updateUser(user, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> removeUser(@PathVariable Long id) {
        service.deleteUser(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

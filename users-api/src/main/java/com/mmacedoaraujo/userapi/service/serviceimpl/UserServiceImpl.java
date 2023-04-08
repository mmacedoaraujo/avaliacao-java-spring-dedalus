package com.mmacedoaraujo.userapi.service.serviceimpl;

import com.mmacedoaraujo.userapi.domain.User;
import com.mmacedoaraujo.userapi.exceptions.domain.UserNotFoundException;
import com.mmacedoaraujo.userapi.mapper.UserMapper;
import com.mmacedoaraujo.userapi.repository.UserRepository;
import com.mmacedoaraujo.userapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public List<User> returnAllUsers() {
        return repository.findAll();
    }

    @Override
    public Page<User> returnAllUsersPageable(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public User findUserById(Long id) {
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException(""));
    }

    @Override
    public User saveNewUser(User user) {
        return repository.save(user);
    }

    @Override
    public void updateUser(User user, Long id) {
        User userFromDatabase = findUserById(id);
        User updatedUser = UserMapper.INSTANCE.updateUser(user, userFromDatabase);
        repository.save(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        findUserById(id);
        repository.deleteById(id);
    }
}

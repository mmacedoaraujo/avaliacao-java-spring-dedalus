package com.mmacedoaraujo.avaliacaojavaspringdedalus.service.serviceimpl;

import com.mmacedoaraujo.avaliacaojavaspringdedalus.domain.User;
import com.mmacedoaraujo.avaliacaojavaspringdedalus.repository.UserRepository;
import com.mmacedoaraujo.avaliacaojavaspringdedalus.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public List<User> returnAllUsers() {
        return null;
    }

    @Override
    public User findUserById(Long id) {
        return null;
    }

    @Override
    public Long saveNewUser(User user) {
        return null;
    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public void deleteUser(Long id) {

    }
}

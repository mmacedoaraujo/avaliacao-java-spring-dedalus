package com.mmacedoaraujo.avaliacaojavaspringdedalus.service.serviceimpl;

import com.mmacedoaraujo.avaliacaojavaspringdedalus.domain.User;
import com.mmacedoaraujo.avaliacaojavaspringdedalus.exceptions.UserNotFoundException;
import com.mmacedoaraujo.avaliacaojavaspringdedalus.mapper.UserMapper;
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
        return repository.findAll();
    }

    @Override
    public User findUserById(Long id) {
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
    }

    @Override
    public Long saveNewUser(User user) {
        return repository.save(user).getId();
    }

    @Override
    public void updateUser(User user) {
        User userFromDatabase = findUserById(user.getId());
        User updatedUser = UserMapper.INSTANCE.updateUser(user, userFromDatabase);
        repository.save(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        findUserById(id);
        repository.deleteById(id);
    }
}

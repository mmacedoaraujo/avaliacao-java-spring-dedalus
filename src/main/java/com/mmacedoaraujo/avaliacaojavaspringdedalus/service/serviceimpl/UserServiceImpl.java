package com.mmacedoaraujo.avaliacaojavaspringdedalus.service.serviceimpl;

import com.mmacedoaraujo.avaliacaojavaspringdedalus.domain.User;
import com.mmacedoaraujo.avaliacaojavaspringdedalus.exceptions.UserNotFoundException;
import com.mmacedoaraujo.avaliacaojavaspringdedalus.mapper.UserMapper;
import com.mmacedoaraujo.avaliacaojavaspringdedalus.repository.UserRepository;
import com.mmacedoaraujo.avaliacaojavaspringdedalus.service.UserService;
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
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
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

package com.mmacedoaraujo.avaliacaojavaspringdedalus.util;

import com.mmacedoaraujo.avaliacaojavaspringdedalus.domain.User;

import java.time.LocalDate;
import java.util.List;

public class UserCreator {

    public static User createUserWithId(String name, String lastName) {
        return User.builder()
                .id(1L)
                .name(name)
                .lastName(lastName)
                .birthDate(LocalDate.now())
                .nationality("Brasileiro")
                .build();
    }

    public static List<User> createListOfUsers() {
        return List.of(createUserWithId("João", "Teste"),
                createUserWithId("Maria", "Teste"),
                createUserWithId("Teste", "Teste"));
    }
}

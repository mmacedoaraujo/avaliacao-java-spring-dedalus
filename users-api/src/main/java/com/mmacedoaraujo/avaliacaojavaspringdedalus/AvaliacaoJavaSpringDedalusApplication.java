package com.mmacedoaraujo.avaliacaojavaspringdedalus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@AllArgsConstructor
@EnableCaching
@EnableAspectJAutoProxy
public class AvaliacaoJavaSpringDedalusApplication {

    public static void main(String[] args) {
        SpringApplication.run(AvaliacaoJavaSpringDedalusApplication.class, args);
    }
}

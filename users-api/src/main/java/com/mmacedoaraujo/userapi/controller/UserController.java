package com.mmacedoaraujo.userapi.controller;

import com.mmacedoaraujo.userapi.config.RabbitMQConfig;
import com.mmacedoaraujo.userapi.domain.User;
import com.mmacedoaraujo.userapi.service.serviceimpl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
@CrossOrigin
public class UserController {

    private final UserServiceImpl service;

    private final RabbitTemplate template;

    @GetMapping("/hello")
    @Operation(summary = "Simply return 'Hello World!'")
    public ResponseEntity<String> returnsHello() {
        return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }

    @GetMapping
    @Cacheable("users")
    @Operation(summary = "List all users", description = "Returns a list of all users on database. Requires no authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success, the service was able to retrieve users from database",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "503", description = "Failure, the gateway was not able to register at Eureka Server yet",
                    content = {@Content(mediaType = "application/json")})
    })
    public ResponseEntity<List<User>> returnUsersRegistered() {
        List<User> users = service.returnAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/paginated")
    @Cacheable("usersPage")
    @Operation(summary = "List all users, but pageable", description = "Returns a page of all users on database. Requires no authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found users",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "503", description = "Failure, the gateway was not able to register at Eureka Server yet",
                    content = {@Content(mediaType = "application/json")})
    })
    public ResponseEntity<Page<User>> returnUsersRegisteredPageable(@ParameterObject Pageable pageable) {
        Page<User> usersPage = service.returnAllUsersPageable(pageable);
        return new ResponseEntity<>(usersPage, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    @Cacheable("userId")
    @Operation(summary = "Search for an user based on it's id", description = "Return an user based on it's registered id. Requires authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found user",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "The id supplied was not valid",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "503", description = "Failure, the gateway was not able to register at Eureka Server yet",
                    content = {@Content(mediaType = "application/json")})
    })
    public ResponseEntity<User> returnUserById(@PathVariable Long id) {
        User userFoundById = service.findUserById(id);
        return new ResponseEntity<>(userFoundById, HttpStatus.OK);
    }

    @PostMapping("/addNewUser")
    @CacheEvict(value = {"users", "usersPage"}, allEntries = true)
    @Operation(summary = "Add new user", description = "Saves an user at database. Requires authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success, user it's now registered at database",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "503", description = "Failure, the gateway was not able to register at Eureka Server yet",
                    content = {@Content(mediaType = "application/json")})
    })
    public ResponseEntity<User> addNewUser(@RequestBody User user) {
        User newUser = service.saveNewUser(user);
        template.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.USER_ROUTING_KEY, newUser);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/updateUser/{id}")
    @CacheEvict(value = {"users", "usersPage"}, allEntries = true)
    @Operation(summary = "Update user's information on database",
            description = "At this endpoint we can pass a complete body for update or only desired fields, if the service can find an user by the supplied id, it will be updated. This endpoint requires authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Success, user's infos were successfuly updated",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "The id supplied was not valid",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "503", description = "Failure, the gateway was not able to register at Eureka Server yet",
                    content = {@Content(mediaType = "application/json")})
    })
    public ResponseEntity<Void> updateUserData(@RequestBody User user, @PathVariable Long id) {
        service.updateUser(user, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/deleteUser/{id}")
    @CacheEvict(value = {"users", "usersPage"}, allEntries = true)
    @Operation(summary = "Delete user's at database",
            description = "If an user successfully matches the supplied id, it will be deleted. This endpoint requires authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Success, user's infos were successfuly updated",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "The id supplied was not valid",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "503", description = "Failure, the gateway was not able to register at Eureka Server yet",
                    content = {@Content(mediaType = "application/json")})
    })
    public ResponseEntity<Void> removeUser(@PathVariable Long id) {
        service.deleteUser(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

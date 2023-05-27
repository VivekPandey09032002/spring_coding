package com.vivek.java_testing.controller;

import com.vivek.java_testing.dto.request.RequestUser;
import com.vivek.java_testing.dto.response.ResponseCollection;
import com.vivek.java_testing.entity.User;
import com.vivek.java_testing.exception.CustomException;
import com.vivek.java_testing.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Log4j2
public class UserController {
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @PostMapping
    public ResponseEntity<ResponseCollection<Object>> saveUser(@RequestBody @Valid RequestUser userDto,
                                                               BindingResult result) {
        if (result.hasErrors()) {
            log.error("error in user dto");
            final var errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new CustomException("cannot register the user", errors, HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsById(userDto.getUserId())) {
            log.error("user already exists");
            throw new CustomException("cannot register the user, already exist",
                    List.of("user id already exists"),
                    HttpStatus.FOUND);
        }
        User user = mapper.map(userDto, User.class);
        userRepository.save(user);
        final var responseBody = ResponseCollection.builder().data(List.of(userDto))
                .message("user saved successfully")
                .data(List.of(userDto)).status(HttpStatus.CREATED).build();
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping
    public ResponseEntity<ResponseCollection<Object>> getUserAllUser() {
        List<User> users = userRepository.findAll();

        final var usersDto = users.stream().map((user) -> mapper.map(user, RequestUser.class)).toList();

        final var responseBody = ResponseCollection.builder().data(List.of(usersDto)).message("users list success")
                .status(HttpStatus.OK).build();
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("{userId}")
    public ResponseEntity<ResponseCollection<Object>> getUser(@PathVariable String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("cannot get the user,no user found",
                        List.of("no a valid userId"), HttpStatus.NOT_FOUND));
        RequestUser userDto = mapper.map(user, RequestUser.class);
        final var responseBody = ResponseCollection.builder().data(List.of(userDto)).message("user respone success")
                .data(List.of(userDto)).status(HttpStatus.CREATED).build();
        return ResponseEntity.ok(responseBody);
    }

}

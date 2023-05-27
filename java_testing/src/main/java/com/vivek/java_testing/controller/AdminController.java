package com.vivek.java_testing.controller;

import com.vivek.java_testing.dto.request.AdminDto;
import com.vivek.java_testing.dto.response.AdminResponse;
import com.vivek.java_testing.dto.response.ResponseSingle;
import com.vivek.java_testing.entity.Admin;
import com.vivek.java_testing.exception.CustomException;
import com.vivek.java_testing.repository.AdminRepository;
import com.vivek.java_testing.utility.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Log4j2
public class AdminController {

    private static final String LOGIN_ERROR_MESSAGE = "Cannot signed in as admin";
    private static final String LOGOUT_ERROR_MESSAGE = "Cannot signed up as admin";
    private final AdminRepository adminRepository;
    private final ModelMapper mapper;
    private final JwtUtil jwt;

    @PostMapping("/register")
    public ResponseEntity<ResponseSingle<AdminResponse>> registerAdmin(@RequestBody @Valid AdminDto adminDto,
                                                                       BindingResult results
    ) {
        // validating dto
        if (results.hasErrors()) {
            log.error("invalid admin dto");
            final var errors = results.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new CustomException(LOGOUT_ERROR_MESSAGE, errors, HttpStatus.BAD_REQUEST);
        }
        final int id = adminDto.getAdminId();
        log.debug("admin id: {}",id);
        Admin admin = mapper.map(adminDto, Admin.class);
        // if admin exists throw error
        if (adminRepository.existsById(id)) {
            log.error("admin already exists with id: {}",id);
            throw new CustomException(LOGOUT_ERROR_MESSAGE, List.of("Admin already exists"), HttpStatus.FOUND);
        }
        //saving admin info
        adminRepository.save(admin);
        log.info("saved admin information");
        // returning the response
        AdminResponse adminResponse = mapper.map(adminDto, AdminResponse.class);

        final var adminRegisterSuccessfully = ResponseSingle.<AdminResponse>builder()
                .data(adminResponse)
                .message("Admin register successfully")
                .status(HttpStatus.CREATED)
                .build();
        log.info("returning register admin response");
        return ResponseEntity.ok(adminRegisterSuccessfully);

    }

    @PostMapping("/sign-in")
    public ResponseEntity<ResponseSingle<AdminResponse>> loginInAdmin(@RequestBody @Valid AdminDto adminDto,
                                                                      BindingResult results,
                                                                      HttpServletResponse response,
                                                                      @CookieValue(value = "token", defaultValue = "") String token
    ) {
        //validating dto
        if (results.hasErrors()) {
            log.error("invalid admin dto");
            final var errors = results.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new CustomException(LOGIN_ERROR_MESSAGE, errors, HttpStatus.BAD_REQUEST);
        }

        // if token exist then no need to create a new token
        final int id = adminDto.getAdminId();
        log.debug("admin id: {}",id);
        if (token.length() != 0 && Boolean.TRUE.equals(jwt.validateToken(token, String.valueOf(id)))) {
            log.error("invalid token");
            final AdminResponse adminResponse = mapper.map(adminDto, AdminResponse.class);
            final var responseBody = ResponseSingle.<AdminResponse>builder().data(adminResponse).message("Admin already logged in").status(HttpStatus.FOUND).build();
            return ResponseEntity.ok(responseBody);

        }
        // if admin didn't exist throw error
        if (!adminRepository.existsById(id)) {
            log.error("no valid admin found with given id");
            throw new CustomException(LOGIN_ERROR_MESSAGE,
                    List.of(MessageFormat.format("No admin found with given user id: {0}", id)),
                    HttpStatus.NOT_FOUND);
        }

        final AdminResponse adminResponse = mapper.map(adminDto, AdminResponse.class);
        //creating token and saving to cookie
        token = jwt.generateToken(String.valueOf(id));
        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge(24 * 60 * 60); //1 day
        response.addCookie(cookie);
        log.info("generating token and saving token to response");

        final var responseBody = ResponseSingle.<AdminResponse>builder().data(adminResponse).message("Admin logged in successfully").status(HttpStatus.CREATED).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @GetMapping("/sign-out")
    public ResponseEntity<String> signOutAdmin(@CookieValue(value = "token", defaultValue = "") String token,
                                               HttpServletResponse response
    ) {
        if (token.length() == 0) {
            log.error("invalid token");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("already logout out");
        }
        Cookie cookie = new Cookie("token", "");
        response.addCookie(cookie);
        log.info("clearing the token");
        return ResponseEntity.ok("logout successfully");

    }

}
